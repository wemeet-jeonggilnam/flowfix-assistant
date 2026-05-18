package com.wemeet.flowfixassistant.chat.presentation.dto

import com.wemeet.flowfixassistant.chat.domain.model.ChatMessage
import com.wemeet.flowfixassistant.chat.domain.model.MessageSource
import com.wemeet.flowfixassistant.chat.domain.vo.TokenUsageInfo

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
