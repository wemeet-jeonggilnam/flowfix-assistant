package com.wemeet.flowfixassistant.chat.domain.repository

import com.wemeet.flowfixassistant.chat.domain.model.TokenUsageLog

interface TokenUsageLogRepository {
    fun save(tokenUsageLog: TokenUsageLog): TokenUsageLog
}
