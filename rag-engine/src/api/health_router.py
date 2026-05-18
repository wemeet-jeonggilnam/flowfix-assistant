from fastapi import APIRouter

from src.config.database import check_connection
from src.core.embeddings import embedding_service
from src.models.schemas import HealthResponse

router = APIRouter()


@router.get("/health", response_model=HealthResponse)
async def health_check():
    db_status = await check_connection()
    components = {
        **db_status,
        "embedding_model": embedding_service.status,
    }
    all_ok = db_status.get("database") == "ok"
    return HealthResponse(
        status="ok" if all_ok else "degraded",
        version="0.1.0",
        components=components,
    )
