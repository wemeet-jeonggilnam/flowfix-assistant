package com.wemeet.flowfixassistant.infrastructure.chat

import com.wemeet.flowfixassistant.domain.chat.Conversation
import org.springframework.data.jpa.repository.JpaRepository

interface ConversationRepository : JpaRepository<Conversation, Long> {
    fun findByUserIdOrderByUpdatedAtDesc(userId: Long): List<Conversation>
}
