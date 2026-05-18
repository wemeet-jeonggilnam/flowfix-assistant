def test_health_endpoint_returns_200(client):
    """DB 없이도 health 엔드포인트가 응답하는지 확인"""
    response = client.get("/health")
    # DB 연결 없으면 degraded 상태지만 HTTP 응답은 200
    assert response.status_code == 200
    data = response.json()
    assert "status" in data
    assert "version" in data
    assert data["version"] == "0.1.0"
    assert "components" in data
