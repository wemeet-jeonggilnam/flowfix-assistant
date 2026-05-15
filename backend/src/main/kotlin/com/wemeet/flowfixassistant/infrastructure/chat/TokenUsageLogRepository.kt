package com.wemeet.flowfixassistant.infrastructure.chat

import com.wemeet.flowfixassistant.domain.chat.TokenUsageLog
import org.springframework.data.jpa.repository.JpaRepository

interface TokenUsageLogRepository : JpaRepository<TokenUsageLog, Long>
