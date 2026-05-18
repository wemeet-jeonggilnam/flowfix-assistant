package com.wemeet.flowfixassistant.chat.presentation.dto

import com.wemeet.flowfixassistant.chat.domain.model.Conversation
import java.time.LocalDateTime

data class MessageListResponse(
    val messages: List<MessageItem>,
) {
    data class MessageItem(
        val id: Long,
        val role: String,
        val content: String,
        val sources: List<SourceInfo>,
        val createdAt: LocalDateTime,
    )

    companion object {
        fun from(conversation: Conversation): MessageListResponse {
            return MessageListResponse(
                messages = conversation.messages.map { msg ->
                    MessageItem(
                        id = msg.id,
                        role = msg.role.name,
                        content = msg.content,
                        sources = msg.sources.map {
                            SourceInfo(it.sourceType, it.sourceName, it.sourceSection, it.relevanceScore, it.snippet)
                        },
                        createdAt = msg.createdAt,
                    )
                }
            )
        }
    }
}
