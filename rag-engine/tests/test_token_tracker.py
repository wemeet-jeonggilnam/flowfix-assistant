import pytest
from datetime import date

from src.core.token_tracker import TokenTracker


@pytest.fixture
def tracker():
    return TokenTracker()


@pytest.mark.asyncio
async def test_record_and_daily_usage(tracker):
    await tracker.record("claude-3.5-sonnet", 1000, 500)
    await tracker.record("claude-3.5-sonnet", 2000, 800)

    usage = await tracker.get_daily_usage()
    assert usage["total_requests"] == 2
    assert usage["total_input_tokens"] == 3000
    assert usage["total_output_tokens"] == 1300
    assert usage["date"] == date.today().isoformat()


@pytest.mark.asyncio
async def test_by_model_breakdown(tracker):
    await tracker.record("claude-3.5-sonnet", 1000, 500)
    await tracker.record("gpt-4o", 800, 300)

    usage = await tracker.get_daily_usage()
    assert "claude-3.5-sonnet" in usage["by_model"]
    assert "gpt-4o" in usage["by_model"]
    assert usage["by_model"]["claude-3.5-sonnet"]["count"] == 1
    assert usage["by_model"]["gpt-4o"]["input_tokens"] == 800


@pytest.mark.asyncio
async def test_empty_tracker(tracker):
    usage = await tracker.get_daily_usage()
    assert usage["total_requests"] == 0
    assert usage["total_input_tokens"] == 0


@pytest.mark.asyncio
async def test_summary(tracker):
    await tracker.record("claude-3.5-sonnet", 500, 200)
    summary = await tracker.get_summary()
    assert summary["total_records"] == 1
    assert summary["today"]["total_requests"] == 1
