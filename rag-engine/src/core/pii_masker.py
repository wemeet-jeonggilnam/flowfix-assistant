"""PII(개인식별정보) 마스킹 모듈.

한국어 환경의 전화번호, 이메일, 주소 패턴을 감지하여 마스킹 처리한다.
LLM API로 전송하기 전에 반드시 이 모듈을 통해 마스킹해야 한다.
"""

import re
from dataclasses import dataclass


@dataclass
class MaskResult:
    text: str
    masked: bool


# 휴대폰: 010-1234-5678, 01012345678, 010.1234.5678
_MOBILE_PATTERN = re.compile(r"(?<!\d)(01[016789])[-.]?(\d{3,4})[-.]?(\d{4})(?!\d)")

# 유선전화: 02-123-4567, 031-123-4567, 0212341234
_LANDLINE_PATTERN = re.compile(r"(?<!\d)(0[2-6][0-9]{0,2})[-.]?(\d{3,4})[-.]?(\d{4})(?!\d)")

# 이메일
_EMAIL_PATTERN = re.compile(r"\b([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+\.[a-zA-Z]{2,})\b")

# 한국 도로명주소 패턴: ~로 숫자, ~길 숫자
_ROAD_ADDRESS_PATTERN = re.compile(
    r"([가-힣]+(?:시|도)\s+[가-힣]+(?:시|군|구)"
    r"(?:\s+[가-힣]+(?:구|동|읍|면))*"
    r"\s+[가-힣0-9]+(?:로|길)\s*\d+[가-힣0-9\-]*)"
)

# 지번주소 패턴: ~동 123-45
_LOT_ADDRESS_PATTERN = re.compile(
    r"([가-힣]+(?:시|도)\s+[가-힣]+(?:시|군|구)"
    r"(?:\s+[가-힣]+(?:구))*"
    r"\s+[가-힣]+(?:동|읍|면|리)\s*\d+(?:-\d+)?)"
)


def _mask_phone(text: str) -> str:
    text = _MOBILE_PATTERN.sub(r"\1-****-\3", text)
    text = _LANDLINE_PATTERN.sub(r"\1-****-\3", text)
    return text


def _mask_email(text: str) -> str:
    def replace_email(match: re.Match) -> str:
        local = match.group(1)
        domain = match.group(2)
        masked_local = local[0] + "***" if local else "***"
        return f"{masked_local}@{domain}"

    return _EMAIL_PATTERN.sub(replace_email, text)


def _mask_address(text: str) -> str:
    text = _ROAD_ADDRESS_PATTERN.sub("[주소 마스킹됨]", text)
    text = _LOT_ADDRESS_PATTERN.sub("[주소 마스킹됨]", text)
    return text


def mask_pii(text: str) -> MaskResult:
    """텍스트에서 PII를 감지하고 마스킹한다."""
    original = text
    text = _mask_phone(text)
    text = _mask_email(text)
    text = _mask_address(text)
    return MaskResult(text=text, masked=(text != original))
