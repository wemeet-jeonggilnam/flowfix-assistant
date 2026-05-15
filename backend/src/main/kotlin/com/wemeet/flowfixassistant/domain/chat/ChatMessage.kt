package com.wemeet.flowfixassistant.domain.chat

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "chat_message")
class ChatMessage(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    val conversation: Conversation,

    @Column(nullable = false, length = 20)
    val role: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,

    @Column(name = "input_tokens")
    val inputTokens: Int? = null,

    @Column(name = "output_tokens")
    val outputTokens: Int? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
