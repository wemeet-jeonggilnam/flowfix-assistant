package com.wemeet.flowfixassistant.chat.presentation.dto

import com.wemeet.flowfixassistant.chat.application.dto.ChatSendResult
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
        fun of(result: ChatSendResult): ChatSendResponse {
            return ChatSendResponse(
                conversationId = result.conversation.id,
                messageId = result.aiMessage.id,
                answer = result.aiMessage.content,
                sources = result.aiMessage.sources.map {
                    SourceInfo(
                        type = it.sourceType,
                        name = it.sourceName,
                        section = it.sourceSection,
                        relevanceScore = it.relevanceScore,
                        snippet = it.snippet,
                    )
                },
                tokenUsage = result.tokenUsage?.let { TokenUsage(it.inputTokens, it.outputTokens) },
                createdAt = result.aiMessage.createdAt,
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
