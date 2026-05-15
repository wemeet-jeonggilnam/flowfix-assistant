package com.wemeet.flowfixassistant.presentation.chat.dto

data class ChatRequest(
    val message: String,
    val conversationId: Long? = null,
)
