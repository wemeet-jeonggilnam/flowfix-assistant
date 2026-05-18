package com.wemeet.flowfixassistant.chat.application.dto

data class ChatSendCommand(
    val message: String,
    val conversationId: Long? = null,
)
