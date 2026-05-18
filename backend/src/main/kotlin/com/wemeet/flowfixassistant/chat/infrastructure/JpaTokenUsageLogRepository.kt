package com.wemeet.flowfixassistant.chat.infrastructure

import com.wemeet.flowfixassistant.chat.domain.model.TokenUsageLog
import com.wemeet.flowfixassistant.chat.domain.repository.TokenUsageLogRepository
import org.springframework.data.jpa.repository.JpaRepository

interface JpaTokenUsageLogRepository : TokenUsageLogRepository, JpaRepository<TokenUsageLog, Long>
