from src.core.pii_masker import mask_pii


class TestPhoneMasking:
    def test_mobile_with_hyphens(self):
        result = mask_pii("연락처: 010-1234-5678")
        assert "1234" not in result.text
        assert "010-****-5678" in result.text
        assert result.masked is True

    def test_mobile_without_hyphens(self):
        result = mask_pii("전화번호 01012345678입니다")
        assert "1234" not in result.text
        assert result.masked is True

    def test_mobile_with_dots(self):
        result = mask_pii("010.9876.5432")
        assert "9876" not in result.text
        assert result.masked is True

    def test_landline_seoul(self):
        result = mask_pii("사무실: 02-1234-5678")
        assert "1234" not in result.text
        assert result.masked is True

    def test_landline_gyeonggi(self):
        result = mask_pii("전화: 031-123-4567")
        assert "123" not in result.text or "031" in result.text
        assert result.masked is True

    def test_other_mobile_prefixes(self):
        result = mask_pii("016-111-2222")
        assert "111" not in result.text
        assert result.masked is True


class TestEmailMasking:
    def test_basic_email(self):
        result = mask_pii("이메일: user@example.com")
        assert "user" not in result.text
        assert "u***@example.com" in result.text
        assert result.masked is True

    def test_email_with_dots(self):
        result = mask_pii("john.doe@company.co.kr")
        assert "john.doe" not in result.text
        assert "@company.co.kr" in result.text
        assert result.masked is True


class TestAddressMasking:
    def test_road_address(self):
        result = mask_pii("배송지: 서울시 강남구 테헤란로 123")
        assert "테헤란로 123" not in result.text
        assert result.masked is True

    def test_lot_address(self):
        result = mask_pii("주소: 서울시 강남구 역삼동 123-45")
        assert "역삼동 123-45" not in result.text
        assert result.masked is True


class TestNoFalsePositives:
    def test_plain_text(self):
        text = "오늘 주문이 50건 접수되었습니다."
        result = mask_pii(text)
        assert result.text == text
        assert result.masked is False

    def test_order_number(self):
        text = "주문번호: ORD-2026-001234"
        result = mask_pii(text)
        assert result.text == text
        assert result.masked is False

    def test_short_numbers(self):
        text = "총 3개 항목, 가격 15000원"
        result = mask_pii(text)
        assert result.text == text
        assert result.masked is False


class TestMixedContent:
    def test_multiple_pii_types(self):
        text = "고객 연락처: 010-1234-5678, 이메일: customer@test.com, 주소: 서울시 강남구 역삼동 123-45"
        result = mask_pii(text)
        assert "1234" not in result.text
        assert "customer" not in result.text
        assert "역삼동 123-45" not in result.text
        assert result.masked is True
