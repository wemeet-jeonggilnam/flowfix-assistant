package com.wemeet.flowfixassistant.conversation.domain.vo

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class TokenUsageInfo(
    @Column(name = "input_tokens")
    val inputTokens: Int = 0,

    @Column(name = "output_tokens")
    val outputTokens: Int = 0,
) {
    fun totalTokens(): Int = inputTokens + outputTokens
}
