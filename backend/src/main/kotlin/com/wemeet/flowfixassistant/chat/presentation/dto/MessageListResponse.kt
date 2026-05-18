package com.wemeet.flowfixassistant.chat.presentation.dto

import java.time.LocalDateTime

data class MessageListResponse(
    val id: Long,
    val role: String,
    val content: String,
    val sources: List<SourceInfo>,
    val createdAt: LocalDateTime,
)
