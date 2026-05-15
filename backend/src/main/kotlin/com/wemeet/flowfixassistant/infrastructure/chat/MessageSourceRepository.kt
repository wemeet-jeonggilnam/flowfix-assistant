package com.wemeet.flowfixassistant.infrastructure.chat

import com.wemeet.flowfixassistant.domain.chat.MessageSource
import org.springframework.data.jpa.repository.JpaRepository

interface MessageSourceRepository : JpaRepository<MessageSource, Long> {
    fun findByMessageId(messageId: Long): List<MessageSource>
}
