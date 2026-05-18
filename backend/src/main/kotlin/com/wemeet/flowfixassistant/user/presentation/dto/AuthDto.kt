package com.wemeet.flowfixassistant.user.presentation.dto

import com.wemeet.flowfixassistant.user.application.dto.AuthResult
import com.wemeet.flowfixassistant.user.application.dto.LoginCommand
import com.wemeet.flowfixassistant.user.application.dto.SignUpCommand
import jakarta.validation.constraints.NotBlank

data class SignUpRequest(
    @field:NotBlank(message = "사용자명은 필수입니다.")
    val username: String,
    val displayName: String? = null,
) {
    fun toCommand() = SignUpCommand(username = username, displayName = displayName)
}

data class LoginRequest(
    @field:NotBlank(message = "사용자명은 필수입니다.")
    val username: String,
) {
    fun toCommand() = LoginCommand(username = username)
}

data class TokenResponse(
    val accessToken: String,
    val username: String,
    val role: String,
) {
    companion object {
        fun from(result: AuthResult): TokenResponse {
            return TokenResponse(accessToken = result.accessToken, username = result.username, role = result.role)
        }
    }
}