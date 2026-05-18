package com.wemeet.flowfixassistant.conversation.domain.repository

import com.wemeet.flowfixassistant.conversation.domain.model.Conversation
import java.util.Optional

interface ConversationRepository {
    fun save(conversation: Conversation): Conversation
    fun findById(id: Long): Optional<Conversation>
    fun findByUserIdOrderByUpdatedAtDesc(userId: Long): List<Conversation>
}
