package com.wemeet.flowfixassistant.presentation.chat.dto

import java.time.LocalDateTime

data class ChatResponse(
    val conversationId: Long,
    val messageId: Long,
    val answer: String,
    val sources: List<SourceInfo>,
    val tokenUsage: TokenUsage?,
    val createdAt: LocalDateTime,
)

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
