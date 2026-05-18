"""Backend(Spring Boot)의 RagRequest/RagResponse JSON 계약과 일치하는 Pydantic 모델.

Jackson(camelCase) <-> Pydantic(snake_case) 변환을 alias로 처리한다.
"""

from pydantic import BaseModel, ConfigDict
from pydantic.alias_generators import to_camel


class CamelModel(BaseModel):
    """camelCase JSON 직렬화를 위한 기본 모델"""

    model_config = ConfigDict(
        alias_generator=to_camel,
        populate_by_name=True,
    )


class MessageDto(CamelModel):
    role: str
    content: str


class ChatRequest(CamelModel):
    """Backend RagRequest와 대응"""

    query: str
    conversation_history: list[MessageDto] = []


class SourceDto(CamelModel):
    """Backend RagSource와 대응"""

    type: str
    name: str
    section: str | None = None
    relevance_score: float | None = None
    snippet: str | None = None


class TokenUsageDto(CamelModel):
    """Backend RagTokenUsage와 대응"""

    input_tokens: int
    output_tokens: int
    model: str


class ChatResponse(CamelModel):
    """Backend RagResponse와 대응"""

    answer: str
    sources: list[SourceDto] = []
    token_usage: TokenUsageDto | None = None


class HealthResponse(BaseModel):
    status: str
    version: str
    components: dict[str, str]
