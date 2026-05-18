package com.wemeet.flowfixassistant.chat.infrastructure

import com.wemeet.flowfixassistant.chat.application.RagClient
import com.wemeet.flowfixassistant.chat.application.dto.RagRequest
import com.wemeet.flowfixassistant.chat.application.dto.RagResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

@Component
class WebClientRagClient(
    private val ragEngineWebClient: WebClient,
) : RagClient {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun query(request: RagRequest): RagResponse {
        return try {
            ragEngineWebClient.post()
                .uri("/api/chat")
                .bodyValue(request)
                .retrieve()
                .bodyToMono<RagResponse>()
                .block()
                ?: RagResponse(answer = "RAG Engine으로부터 응답을 받지 못했습니다.")
        } catch (e: Exception) {
            log.error("RAG Engine 호출 실패: ${e.message}", e)
            RagResponse(answer = "검색 서비스가 일시적으로 이용 불가합니다. 잠시 후 다시 시도해주세요.")
        }
    }
}
