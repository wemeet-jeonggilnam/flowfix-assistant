package com.wemeet.flowfixassistant.conversation.application.dto

import com.wemeet.flowfixassistant.conversation.domain.model.ChatMessage
import com.wemeet.flowfixassistant.conversation.domain.model.MessageSource
import com.wemeet.flowfixassistant.conversation.domain.vo.TokenUsageInfo

data class RagResponse(
    val answer: String,
    val sources: List<RagSource> = emptyList(),
    val tokenUsage: RagTokenUsage? = null,
) {
    fun toMessageSources(message: ChatMessage): List<MessageSource> {
        return sources.map { src ->
            MessageSource(
                message = message,
                sourceType = src.type,
                sourceName = src.name,
                sourceSection = src.section,
                relevanceScore = src.relevanceScore,
                snippet = src.snippet,
            )
        }
    }
}

data class RagSource(
    val type: String,
    val name: String,
    val section: String? = null,
    val relevanceScore: Double? = null,
    val snippet: String? = null,
)

data class RagTokenUsage(
    val inputTokens: Int,
    val outputTokens: Int,
    val model: String,
) {
    fun toTokenUsageInfo(): TokenUsageInfo {
        return TokenUsageInfo(inputTokens = inputTokens, outputTokens = outputTokens)
    }
}
