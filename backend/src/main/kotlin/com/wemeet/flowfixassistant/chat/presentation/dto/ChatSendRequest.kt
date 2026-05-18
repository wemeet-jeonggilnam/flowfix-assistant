package com.wemeet.flowfixassistant.chat.presentation.dto

data class ChatSendRequest(
    val message: String,
    val conversationId: Long? = null,
)
