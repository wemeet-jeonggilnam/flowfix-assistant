"""문서 인덱싱 엔드포인트 (Phase 1 Step 5 구현 예정)"""

from fastapi import APIRouter, HTTPException

router = APIRouter()


@router.post("/api/index")
async def index_documents():
    raise HTTPException(status_code=501, detail="문서 인덱싱은 Phase 1에서 구현 예정입니다.")
