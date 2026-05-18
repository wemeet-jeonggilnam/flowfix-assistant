package com.wemeet.flowfixassistant.conversation.infrastructure.persistence.jpa

import com.wemeet.flowfixassistant.conversation.domain.model.TokenUsageLog
import com.wemeet.flowfixassistant.conversation.domain.repository.TokenUsageLogRepository
import org.springframework.data.jpa.repository.JpaRepository

interface JpaTokenUsageLogRepository : TokenUsageLogRepository, JpaRepository<TokenUsageLog, Long>
