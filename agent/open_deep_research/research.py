import os
os.environ["GOOGLE_API_KEY"] = "AIzaSyDC-ReFAGsSsA92v1VlV4YvTblshCk8ebQ"
os.environ["TAVILY_API_KEY"] = "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"

from typing import Literal

from langchain.chat_models import init_chat_model
from langchain_core.messages import HumanMessage, SystemMessage
from langchain_core.runnables import RunnableConfig

from langgraph.constants import Send
from langgraph.graph import START, END, StateGraph
from langgraph.types import interrupt, Command

from state import (
    ReportStateInput,
    ReportStateOutput,
    Sections,
    ReportState,
    SectionState,
    SectionOutputState,
    Queries,
    Feedback
)

from prompts import (
    report_planner_query_writer_instructions,
    report_planner_instructions,
    query_writer_instructions, 
    section_writer_instructions,
    final_section_writer_instructions,
    section_grader_instructions,
    section_writer_inputs
)

from configuration import Configuration

from utils import (
    format_sections, 
    get_config_value, 
    get_search_params, 
    select_and_execute_search
)

from graph import (
    generate_queries,
    search_web,
    write_section,
    generate_report_plan,
    gather_completed_sections,
    write_final_sections,
    compile_final_report,
    initiate_final_section_writing,
    send_to_web_search
)

# Add nodes 
section_builder = StateGraph(SectionState, output=SectionOutputState)
section_builder.add_node("generate_queries", generate_queries)
section_builder.add_node("search_web", search_web)
section_builder.add_node("write_section", write_section)

# Add edges
section_builder.add_edge(START, "generate_queries")
section_builder.add_edge("generate_queries", "search_web")
section_builder.add_edge("search_web", "write_section")

# Outer graph for initial report plan compiling results from each section -- 
builder = StateGraph(ReportState, input=ReportStateInput, output=ReportStateOutput, config_schema=Configuration)
builder.add_node("generate_report_plan", generate_report_plan)
builder.add_node("build_section_with_web_research", section_builder.compile())
builder.add_node("gather_completed_sections", gather_completed_sections)
builder.add_node("write_final_sections", write_final_sections)
builder.add_node("compile_final_report", compile_final_report)

# Add edges
builder.add_edge(START, "generate_report_plan")
builder.add_conditional_edges("generate_report_plan", send_to_web_search, ["build_section_with_web_research"])
builder.add_edge("build_section_with_web_research", "gather_completed_sections")
builder.add_conditional_edges("gather_completed_sections", initiate_final_section_writing, ["write_final_sections"])
builder.add_edge("write_final_sections", "compile_final_report")
builder.add_edge("compile_final_report", END)

from langgraph.checkpoint.memory import MemorySaver
import uuid
memory = MemorySaver()
graph = builder.compile(checkpointer=memory)

REPORT_STRUCTURE = """Use this structure to create a report on the user-provided topic:

1. Introduction (no research needed)
   - Brief overview of the topic area

2. Main Body Sections:
   - Each section should focus on a sub-topic of the user-provided topic
   
3. Conclusion
   - Aim for 1 structural element (either a list of table) that distills the main body sections 
   - Provide a concise summary of the report"""

thread = {"configurable": {"thread_id": str(uuid.uuid4()),
                           "max_search_depth": 2,
                           "report_structure": REPORT_STRUCTURE,
                           }}

# Run the graph asynchronously
async def run_graph():
    events = graph.astream({"topic": "How to build AI Agent with langgraph"}, thread, stream_mode="updates")
    async for event in events:
        print(event)
    
    # Get final report after completion
    final_state = graph.get_state(thread)
    report = final_state.values.get('final_report')
    print(report)

# Execute the async function
import asyncio
asyncio.run(run_graph())
