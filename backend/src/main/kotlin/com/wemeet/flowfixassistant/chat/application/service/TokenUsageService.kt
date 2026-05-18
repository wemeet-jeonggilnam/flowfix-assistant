package com.wemeet.flowfixassistant.chat.application.service

import com.wemeet.flowfixassistant.chat.application.dto.RagTokenUsage
import com.wemeet.flowfixassistant.chat.domain.model.TokenUsageLog
import com.wemeet.flowfixassistant.chat.domain.repository.TokenUsageLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class TokenUsageService(
    private val tokenUsageLogRepository: TokenUsageLogRepository,
) {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun record(userId: Long, tokenUsage: RagTokenUsage?) {
        tokenUsage?.let {
            tokenUsageLogRepository.save(
                TokenUsageLog(
                    userId = userId,
                    model = it.model,
                    inputTokens = it.inputTokens,
                    outputTokens = it.outputTokens,
                )
            )
        }
    }
}
