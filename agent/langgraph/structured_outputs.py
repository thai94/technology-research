import os
from langchain.chat_models import init_chat_model

os.environ["GOOGLE_API_KEY"] = "AIzaSyDC-ReFAGsSsA92v1VlV4YvTblshCk8ebQ"
llm = init_chat_model("google_genai:gemini-2.0-flash")


from pydantic import BaseModel, Field
class ResponseFormatter(BaseModel):
    """Always use this tool to structure your response to the user."""
    answer: str = Field(description="The answer to the user's question")
    followup_question: str = Field(description="A followup question the user could ask")

structured_llm = llm.with_structured_output(ResponseFormatter)
output = structured_llm.invoke("How to learn english effectively?")
print(output)