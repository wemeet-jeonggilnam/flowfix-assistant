package com.wemeet.flowfixassistant.chat.presentation.dto

data class RagRequest(
    val query: String,
    val conversationHistory: List<MessageDto> = emptyList(),
)

data class MessageDto(
    val role: String,
    val content: String,
)
