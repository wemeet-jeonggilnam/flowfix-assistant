package com.wemeet.flowfixassistant.user.presentation.dto

import com.wemeet.flowfixassistant.user.domain.model.AssistantUser
import jakarta.validation.constraints.NotBlank

data class SignUpRequest(
    @field:NotBlank(message = "사용자명은 필수입니다.")
    val username: String,
    val displayName: String? = null,
)

data class LoginRequest(
    @field:NotBlank(message = "사용자명은 필수입니다.")
    val username: String,
)

data class TokenResponse(
    val accessToken: String,
    val username: String,
    val role: String,
) {
    companion object {
        fun of(user: AssistantUser, token: String): TokenResponse {
            return TokenResponse(accessToken = token, username = user.username, role = user.role)
        }
    }
}
