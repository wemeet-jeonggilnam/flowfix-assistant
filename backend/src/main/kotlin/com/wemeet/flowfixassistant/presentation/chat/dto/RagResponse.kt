package com.wemeet.flowfixassistant.presentation.chat.dto

data class RagResponse(
    val answer: String,
    val sources: List<RagSource> = emptyList(),
    val tokenUsage: RagTokenUsage? = null,
)

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
)
