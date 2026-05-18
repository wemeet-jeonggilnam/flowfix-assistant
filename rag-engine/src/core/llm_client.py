"""LLM 클라이언트 인터페이스.

Phase 0에서는 인터페이스 정의만. Phase 1 Step 6에서 Claude/OpenAI 구현 예정.
"""

from dataclasses import dataclass
from typing import Protocol


@dataclass
class LLMResponse:
    answer: str
    input_tokens: int
    output_tokens: int
    model: str


class LLMClient(Protocol):
    async def generate(self, prompt: str, context: str) -> LLMResponse: ...


class ClaudeClient:
    """Claude API 클라이언트 (Phase 1 구현 예정)"""

    async def generate(self, prompt: str, context: str) -> LLMResponse:
        raise NotImplementedError("Phase 1 Step 6에서 구현 예정")


class OpenAIClient:
    """OpenAI API 클라이언트 (Phase 1 구현 예정, Secondary LLM)"""

    async def generate(self, prompt: str, context: str) -> LLMResponse:
        raise NotImplementedError("Phase 1 Step 6에서 구현 예정")
