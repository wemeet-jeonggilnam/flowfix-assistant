package com.wemeet.flowfixassistant.chat.application.dto

import com.wemeet.flowfixassistant.chat.domain.model.ChatMessage
import com.wemeet.flowfixassistant.chat.domain.model.Conversation

data class ChatSendResult(
    val conversation: Conversation,
    val aiMessage: ChatMessage,
    val tokenUsage: RagTokenUsage?,
)
