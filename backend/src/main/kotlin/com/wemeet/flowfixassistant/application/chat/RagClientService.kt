package com.wemeet.flowfixassistant.application.chat

import com.wemeet.flowfixassistant.presentation.chat.dto.RagRequest
import com.wemeet.flowfixassistant.presentation.chat.dto.RagResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class RagClientService(
    private val ragEngineRestClient: RestClient,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun query(request: RagRequest): RagResponse {
        return try {
            ragEngineRestClient.post()
                .uri("/api/chat")
                .body(request)
                .retrieve()
                .body(RagResponse::class.java)
                ?: RagResponse(answer = "RAG Engine으로부터 응답을 받지 못했습니다.")
        } catch (e: Exception) {
            log.error("RAG Engine 호출 실패: ${e.message}", e)
            RagResponse(answer = "검색 서비스가 일시적으로 이용 불가합니다. 잠시 후 다시 시도해주세요.")
        }
    }
}
