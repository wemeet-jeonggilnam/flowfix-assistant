"""임베딩 모델 로더.

multilingual-e5-large 모델을 로드하고 텍스트를 벡터로 변환한다.
첫 로드 시 ~2.2GB 모델을 다운로드한다.
"""

import logging
from sentence_transformers import SentenceTransformer

from src.config.settings import settings

logger = logging.getLogger(__name__)


class EmbeddingService:
    def __init__(self) -> None:
        self._model: SentenceTransformer | None = None
        self._loading = False

    @property
    def is_loaded(self) -> bool:
        return self._model is not None

    @property
    def status(self) -> str:
        if self._model is not None:
            return "ok"
        if self._loading:
            return "loading"
        return "not_loaded"

    def preload(self) -> None:
        """모델을 미리 로드한다. 앱 시작 시 호출."""
        if self._model is not None:
            return
        self._loading = True
        try:
            logger.info("임베딩 모델 로딩 시작: %s", settings.embedding_model)
            self._model = SentenceTransformer(
                settings.embedding_model,
                device=settings.embedding_device,
            )
            logger.info("임베딩 모델 로딩 완료 (dim=%d)", self._model.get_sentence_embedding_dimension())
        except Exception:
            logger.exception("임베딩 모델 로딩 실패")
            raise
        finally:
            self._loading = False

    def encode(self, texts: list[str]) -> list[list[float]]:
        """텍스트 리스트를 벡터로 변환한다."""
        if self._model is None:
            self.preload()
        assert self._model is not None
        embeddings = self._model.encode(texts, normalize_embeddings=True)
        return embeddings.tolist()

    @property
    def dimension(self) -> int:
        """임베딩 벡터 차원 수 (multilingual-e5-large: 1024)"""
        if self._model is None:
            return 1024  # multilingual-e5-large 기본값
        return self._model.get_sentence_embedding_dimension()


# 싱글톤
embedding_service = EmbeddingService()
