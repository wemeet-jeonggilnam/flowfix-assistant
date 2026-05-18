import logging
from contextlib import asynccontextmanager

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from prometheus_fastapi_instrumentator import Instrumentator

from src.api.routes import register_routers
from src.config.settings import settings
from src.middleware.error_handler import register_error_handlers
from src.middleware.logging_middleware import RequestLoggingMiddleware

logging.basicConfig(
    level=getattr(logging, settings.log_level),
    format="%(asctime)s [%(levelname)s] %(name)s: %(message)s",
)
logger = logging.getLogger(__name__)


@asynccontextmanager
async def lifespan(app: FastAPI):
    logger.info("FlowFix RAG Engine 시작 (port=%d)", settings.server_port)
    # 임베딩 모델은 첫 요청 시 lazy 로드 (시작 시간 단축)
    yield
    logger.info("FlowFix RAG Engine 종료")


app = FastAPI(
    title="FlowFix RAG Engine",
    version="0.1.0",
    description="문서/DB 검색 및 LLM 답변 생성 엔진",
    lifespan=lifespan,
)

# Middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=[settings.backend_origin],
    allow_methods=["*"],
    allow_headers=["*"],
)
app.add_middleware(RequestLoggingMiddleware)

# Prometheus metrics
Instrumentator().instrument(app).expose(app)

# Routes
register_routers(app)
register_error_handlers(app)
