package com.wemeet.flowfixassistant.conversation.application.dto

import com.wemeet.flowfixassistant.conversation.domain.model.ChatMessage
import com.wemeet.flowfixassistant.conversation.domain.model.Conversation

data class ChatSendResult(
    val conversation: Conversation,
    val aiMessage: ChatMessage,
    val tokenUsage: RagTokenUsage?,
)
