"""pgvector 연동 모듈.

Phase 0에서는 연결 확인만 수행. Phase 1에서 문서 임베딩 CRUD 구현 예정.
"""

from datetime import datetime

from pgvector.sqlalchemy import Vector
from sqlalchemy import JSON, DateTime, Integer, String, Text, func
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column


class Base(DeclarativeBase):
    pass


class DocumentEmbedding(Base):
    """문서 임베딩 테이블 모델 (Phase 1에서 마이그레이션 추가 예정)"""

    __tablename__ = "document_embedding"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    content: Mapped[str] = mapped_column(Text, nullable=False)
    embedding: Mapped[list[float]] = mapped_column(Vector(1024), nullable=False)
    metadata_: Mapped[dict] = mapped_column("metadata", JSON, nullable=True)
    source_type: Mapped[str] = mapped_column(String(50), nullable=False)
    source_name: Mapped[str] = mapped_column(String(500), nullable=False)
    source_section: Mapped[str | None] = mapped_column(String(500), nullable=True)
    created_at: Mapped[datetime] = mapped_column(DateTime, server_default=func.now())
