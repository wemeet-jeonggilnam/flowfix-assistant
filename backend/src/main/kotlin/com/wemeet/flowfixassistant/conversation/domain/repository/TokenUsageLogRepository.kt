package com.wemeet.flowfixassistant.conversation.domain.repository

import com.wemeet.flowfixassistant.conversation.domain.model.TokenUsageLog

interface TokenUsageLogRepository {
    fun save(tokenUsageLog: TokenUsageLog): TokenUsageLog
}
