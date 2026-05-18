"""채팅 엔드포인트.

Phase 0에서는 stub 응답을 반환. Phase 1에서 실제 RAG 체인 연동 예정.
"""

from fastapi import APIRouter

from src.models.schemas import ChatRequest, ChatResponse

router = APIRouter()


@router.post("/api/chat", response_model=ChatResponse, response_model_by_alias=True)
async def chat(request: ChatRequest):
    return ChatResponse(
        answer="RAG Engine이 준비 중입니다. Phase 1에서 문서 검색이 활성화됩니다.",
        sources=[],
        token_usage=None,
    )
