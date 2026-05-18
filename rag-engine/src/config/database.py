from sqlalchemy.ext.asyncio import AsyncSession, async_sessionmaker, create_async_engine
from sqlalchemy import text

from src.config.settings import settings

engine = create_async_engine(
    settings.database_url,
    pool_size=3,
    max_overflow=7,
    echo=False,
)

async_session = async_sessionmaker(engine, class_=AsyncSession, expire_on_commit=False)


async def check_connection() -> dict[str, str]:
    """DB 연결 및 pgvector 확장 상태 확인"""
    result = {"database": "error", "pgvector": "error"}
    try:
        async with engine.connect() as conn:
            await conn.execute(text("SELECT 1"))
            result["database"] = "ok"

            row = await conn.execute(text("SELECT extversion FROM pg_extension WHERE extname = 'vector'"))
            version = row.scalar()
            if version:
                result["pgvector"] = f"ok (v{version})"
            else:
                result["pgvector"] = "not_installed"
    except Exception as e:
        result["database"] = f"error: {e}"
    return result
