"""Backend JSON 계약(camelCase) 호환성 테스트"""


def test_chat_returns_valid_response(client):
    response = client.post("/api/chat", json={
        "query": "주문 상태 흐름이 어떻게 되나요?",
        "conversationHistory": [
            {"role": "user", "content": "안녕하세요"},
        ],
    })
    assert response.status_code == 200
    data = response.json()

    # camelCase 필드명 확인 (Backend Jackson 계약)
    assert "answer" in data
    assert "sources" in data
    assert "tokenUsage" in data  # camelCase
    assert isinstance(data["sources"], list)


def test_chat_without_history(client):
    response = client.post("/api/chat", json={"query": "테스트"})
    assert response.status_code == 200


def test_chat_empty_query_fails(client):
    response = client.post("/api/chat", json={})
    assert response.status_code == 422
