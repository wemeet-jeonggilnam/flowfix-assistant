package com.wemeet.flowfixassistant.chat.presentation.dto

import com.wemeet.flowfixassistant.chat.domain.model.ChatMessage
import com.wemeet.flowfixassistant.chat.domain.model.Conversation
import java.time.LocalDateTime

data class ChatSendResponse(
    val conversationId: Long,
    val messageId: Long,
    val answer: String,
    val sources: List<SourceInfo>,
    val tokenUsage: TokenUsage?,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(conversation: Conversation, aiMessage: ChatMessage, tokenUsage: RagTokenUsage?): ChatSendResponse {
            return ChatSendResponse(
                conversationId = conversation.id,
                messageId = aiMessage.id,
                answer = aiMessage.content,
                sources = aiMessage.sources.map {
                    SourceInfo(
                        type = it.sourceType,
                        name = it.sourceName,
                        section = it.sourceSection,
                        relevanceScore = it.relevanceScore,
                        snippet = it.snippet,
                    )
                },
                tokenUsage = tokenUsage?.let { TokenUsage(it.inputTokens, it.outputTokens) },
                createdAt = aiMessage.createdAt,
            )
        }
    }
}

data class SourceInfo(
    val type: String,
    val name: String,
    val section: String?,
    val relevanceScore: Double?,
    val snippet: String?,
)

data class TokenUsage(
    val inputTokens: Int,
    val outputTokens: Int,
)
