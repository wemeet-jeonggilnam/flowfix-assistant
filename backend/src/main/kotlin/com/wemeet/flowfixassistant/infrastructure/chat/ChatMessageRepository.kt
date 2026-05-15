package com.wemeet.flowfixassistant.infrastructure.chat

import com.wemeet.flowfixassistant.domain.chat.ChatMessage
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    fun findByConversationIdOrderByCreatedAtAsc(conversationId: Long): List<ChatMessage>
}
