package com.wemeet.flowfixassistant.chat.domain.model

import com.wemeet.flowfixassistant.common.domain.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "token_usage_log")
class TokenUsageLog(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(nullable = false, length = 100)
    val model: String,

    @Column(name = "input_tokens", nullable = false)
    val inputTokens: Int,

    @Column(name = "output_tokens", nullable = false)
    val outputTokens: Int,
) : BaseEntity()
