package com.wemeet.flowfixassistant.domain.chat

import com.wemeet.flowfixassistant.domain.user.AssistantUser
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "token_usage_log")
class TokenUsageLog(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: AssistantUser,

    @Column(nullable = false, length = 100)
    val model: String,

    @Column(name = "input_tokens", nullable = false)
    val inputTokens: Int,

    @Column(name = "output_tokens", nullable = false)
    val outputTokens: Int,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
