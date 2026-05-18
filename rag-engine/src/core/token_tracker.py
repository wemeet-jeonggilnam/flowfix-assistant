"""토큰 사용량 추적 모듈.

요청별 LLM 토큰 사용량을 인메모리로 기록하고 일별 집계를 제공한다.
Phase 1에서 DB 영속화로 전환 예정.
"""

import asyncio
from collections import defaultdict
from dataclasses import dataclass, field
from datetime import date, datetime


@dataclass
class TokenRecord:
    model: str
    input_tokens: int
    output_tokens: int
    timestamp: datetime = field(default_factory=datetime.now)


class TokenTracker:
    def __init__(self) -> None:
        self._records: list[TokenRecord] = []
        self._lock = asyncio.Lock()

    async def record(self, model: str, input_tokens: int, output_tokens: int) -> None:
        async with self._lock:
            self._records.append(TokenRecord(
                model=model,
                input_tokens=input_tokens,
                output_tokens=output_tokens,
            ))

    async def get_daily_usage(self, target_date: date | None = None) -> dict:
        target = target_date or date.today()
        async with self._lock:
            daily = [r for r in self._records if r.timestamp.date() == target]

        by_model: dict[str, dict[str, int]] = defaultdict(lambda: {"input_tokens": 0, "output_tokens": 0, "count": 0})
        for r in daily:
            by_model[r.model]["input_tokens"] += r.input_tokens
            by_model[r.model]["output_tokens"] += r.output_tokens
            by_model[r.model]["count"] += 1

        total_input = sum(r.input_tokens for r in daily)
        total_output = sum(r.output_tokens for r in daily)

        return {
            "date": target.isoformat(),
            "total_requests": len(daily),
            "total_input_tokens": total_input,
            "total_output_tokens": total_output,
            "by_model": dict(by_model),
        }

    async def get_summary(self) -> dict:
        async with self._lock:
            total = len(self._records)
        return {
            "total_records": total,
            "today": await self.get_daily_usage(),
        }


# 싱글톤
tracker = TokenTracker()
