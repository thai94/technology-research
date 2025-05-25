import os
from langchain.chat_models import init_chat_model
from typing_extensions import Literal
from langgraph.graph import StateGraph, START, END
from IPython.display import Image, display
from langchain_core.tools import tool
from langchain_core.messages import SystemMessage, ToolMessage, HumanMessage
from langgraph.graph.message import MessagesState

os.environ["GOOGLE_API_KEY"] = "AIzaSyDC-ReFAGsSsA92v1VlV4YvTblshCk8ebQ"
llm = init_chat_model("google_genai:gemini-2.0-flash")

# Define tools
@tool
def multiply(a: int, b: int) -> int:
    """Multiply a and b.

    Args:
        a: first int
        b: second int
    """
    return a * b

@tool
def add(a: int, b: int) -> int:
    """Adds a and b.

    Args:
        a: first int
        b: second int
    """
    return a + b

@tool
def divide(a: int, b: int) -> float:
    """Divide a and b.

    Args:
        a: first int
        b: second int
    """
    return a / b

tools = [add, multiply, divide]
tools_by_name = {tool.name: tool for tool in tools}
llm_with_tools = llm.bind_tools(tools)

def llm_call(state: MessagesState):
    """LLM decides whether to call a tool or not"""

    return {
        "messages": [
            llm_with_tools.invoke(
                [
                    SystemMessage(
                        content="You are a helpful assistant tasked with performing arithmetic on a set of inputs."
                    )
                ]
                + state["messages"]
            )
        ]
    }

def tool_node(state: dict):
    """Performs the tool call"""

    result = []
    for tool_call in state["messages"][-1].tool_calls:
        print('performing tool call: ', tool_call["name"], tool_call["args"])
        tool = tools_by_name[tool_call["name"]]
        observation = tool.invoke(tool_call["args"])
        print('observation: ', observation)
        result.append(ToolMessage(content=observation, tool_call_id=tool_call["id"]))
    return {"messages": result}

def should_continue(state: MessagesState) -> Literal["environment", "end"]:
    """Decide if we should continue the loop or stop based upon whether the LLM made a tool call"""

    messages = state["messages"]
    last_message = messages[-1]
    # If the LLM makes a tool call, then perform an action
    if last_message.tool_calls:
        return "Action"
    # Otherwise, we stop (reply to the user)
    return "end"


agent_builder = StateGraph(MessagesState)

agent_builder.add_node("llm_call", llm_call)
agent_builder.add_node("environment", tool_node)


agent_builder.add_edge(START, "llm_call")
agent_builder.add_conditional_edges(
    "llm_call",
    should_continue,
    {
        # Name returned by should_continue : Name of next node to visit
        "Action": "environment",
        "end": END,
    },
)

agent_builder.add_edge("environment", "llm_call")

agent = agent_builder.compile()

try:
    graph_image = agent.get_graph().draw_mermaid_png()
    with open("agent.png", "wb") as f:
        f.write(graph_image)
    print("Graph image saved as 'agent.png'")
except Exception:
    pass

messages = [HumanMessage(content="Add 3 and 4.")]
messages = agent.invoke({"messages": messages})
for m in messages["messages"]:
    m.pretty_print()