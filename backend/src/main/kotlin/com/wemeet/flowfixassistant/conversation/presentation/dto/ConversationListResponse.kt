package com.wemeet.flowfixassistant.conversation.presentation.dto

import com.wemeet.flowfixassistant.conversation.domain.model.Conversation
import java.time.LocalDateTime

data class ConversationListResponse(
    val conversations: List<ConversationItem>,
) {
    data class ConversationItem(
        val id: Long,
        val title: String?,
        val updatedAt: LocalDateTime,
    )

    companion object {
        fun from(conversations: List<Conversation>): ConversationListResponse {
            return ConversationListResponse(
                conversations = conversations.map {
                    ConversationItem(id = it.id, title = it.title, updatedAt = it.updatedAt)
                }
            )
        }
    }
}
