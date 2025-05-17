from typing import Annotated
from typing_extensions import TypedDict

from langgraph.graph import StateGraph, START, END
from langgraph.graph.message import add_messages

class State(TypedDict):
    messages: Annotated[list, add_messages]

graph_builder = StateGraph(State)

import os
from langchain.chat_models import init_chat_model

os.environ["GOOGLE_API_KEY"] = "AIzaSyDC-ReFAGsSsA92v1VlV4YvTblshCk8ebQ"
os.environ["TAVILY_API_KEY"] = "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"

from langchain_tavily import TavilySearch
searchTool = TavilySearch(max_results=5)


llm = init_chat_model("google_genai:gemini-2.0-flash")

import json
from langchain_core.messages import ToolMessage

class BasicToolNode:
    def __init__(self, tools: list) -> None:
        self.tools_by_name = {tool.name: tool for tool in tools}
    def __call__(self, inputs: dict):
        if messages := inputs.get("messages", []):
             message = messages[-1]
        else:
            raise ValueError("No message found in input")
        outputs = []
        for tool_call in message.tool_calls:
            tool_result = self.tools_by_name[tool_call["name"]].invoke(
                tool_call["args"]
            )
            outputs.append(
                ToolMessage(
                    content=json.dumps(tool_result),
                    name=tool_call["name"],
                    tool_call_id=tool_call["id"],
                )
            )
        return {"messages": messages + outputs}


def route_tools(
    state: State,
):
    if isinstance(state, list):
        ai_message = state[-1]
    elif messages := state.get("messages", []):
        ai_message = messages[-1]
    else:
        raise ValueError(f"No messages found in input state to tool_edge: {state}")
    if hasattr(ai_message, "tool_calls") and len(ai_message.tool_calls) > 0:
        return "tools"
    return END



def chatbot(state: State):
    return {"messages": [llm_with_tools.invoke(state["messages"])]}

import sqlite3
import requests
from langchain_community.utilities.sql_database import SQLDatabase
from sqlalchemy import create_engine
from sqlalchemy.pool import StaticPool

def get_engine_for_chinook_db():
    """Pull sql file, populate in-memory database, and create engine."""
    url = "https://raw.githubusercontent.com/lerocha/chinook-database/master/ChinookDatabase/DataSources/Chinook_Sqlite.sql"
    response = requests.get(url)
    sql_script = response.text

    connection = sqlite3.connect(":memory:", check_same_thread=False)
    connection.executescript(sql_script)
    print("Chinook database loaded into memory.")
    return create_engine(
        "sqlite://",
        creator=lambda: connection,
        poolclass=StaticPool,
        connect_args={"check_same_thread": False},
    )

engine = get_engine_for_chinook_db()
db = SQLDatabase(engine)

# from langchain_community.tools.sql_database.tool import (
#     InfoSQLDatabaseTool,
#     ListSQLDatabaseTool,
#     QuerySQLCheckerTool,
#     QuerySQLDatabaseTool,
# )
# from langchain import hub
# prompt_template = hub.pull("langchain-ai/sql-agent-system-prompt")
# system_message = prompt_template.format(dialect="SQLite", top_k=5)

from langchain_community.agent_toolkits.sql.toolkit import SQLDatabaseToolkit

toolkit = SQLDatabaseToolkit(db=db, llm=llm)


tools = toolkit.get_tools()
tools = tools + [searchTool]

from langchain_core.tools import tool
from langgraph.types import Command, interrupt

@tool
def human_assistance(query: str) -> str:
    """Request assistance from a human."""
    human_response = interrupt({"query": query})
    # print(human_response)
    return human_response["data"]

tools = tools + [human_assistance]


llm_with_tools = llm.bind_tools(tools)

tool_node = BasicToolNode(tools=tools)
graph_builder.add_node("chatbot", chatbot)
graph_builder.add_node("tools", tool_node)

graph_builder.add_edge(START, "chatbot")

graph_builder.add_conditional_edges(
    "chatbot",
    route_tools,
    # The following dictionary lets you tell the graph to interpret the condition's outputs as a specific node
    # It defaults to the identity function, but if you
    # want to use a node named something else apart from "tools",
    # You can update the value of the dictionary to something else
    # e.g., "tools": "my_tools"
    {"tools": "tools", END: END},
)

graph_builder.add_edge("tools", "chatbot")

from langgraph.checkpoint.memory import MemorySaver
memory = MemorySaver()


graph = graph_builder.compile(checkpointer=memory)


def stream_graph_updates(user_input: str):
    config = {"configurable": {"thread_id":  "1"}}
    for event in graph.stream({"messages": [{"role": "user", "content": user_input}]},  config):
        for value in event.values():
            print("Assistant:", value["messages"][-1].content)
from IPython.display import Image, display

try:
    graph_image = graph.get_graph().draw_mermaid_png()
    with open("chatbot_graph.png", "wb") as f:
        f.write(graph_image)
    display(Image(graph_image))
    print("Graph image saved as 'chatbot_graph.png'")
except Exception:
    pass

# while True:
#     try:
#         user_input = input("User: ")
#         if user_input.lower() in ["quit", "exit", "q"]:
#             break
#         stream_graph_updates(user_input)
#     except Exception as e:
#         print(f"Error: {e}")

user_input = "When Iphone 17 release will be?"
config = {"configurable": {"thread_id": "1"}}

events = graph.stream(
    {"messages": [{"role": "user", "content": user_input}]},
    config,
    stream_mode="values",
)
for event in events:
    if "messages" in event:
        event["messages"][-1].pretty_print()

# human_response = (
#     "We, the experts are here to help! We'd recommend you check out LangGraph to build your agent."
#     " It's much more reliable and extensible than simple autonomous agents."
# )
# human_command = Command(resume={"data": human_response})
# events = graph.stream(human_command, config, stream_mode="values")
# for event in events:
#     if "messages" in event:
#         event["messages"][-1].pretty_print()