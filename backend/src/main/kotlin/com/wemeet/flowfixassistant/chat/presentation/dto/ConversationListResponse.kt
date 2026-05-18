package com.wemeet.flowfixassistant.chat.presentation.dto

import java.time.LocalDateTime

data class ConversationListResponse(
    val id: Long,
    val title: String?,
    val updatedAt: LocalDateTime,
)
