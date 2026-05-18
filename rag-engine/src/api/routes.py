from fastapi import FastAPI

from src.api.chat_router import router as chat_router
from src.api.health_router import router as health_router
from src.api.index_router import router as index_router


def register_routers(app: FastAPI) -> None:
    app.include_router(health_router)
    app.include_router(chat_router)
    app.include_router(index_router)
