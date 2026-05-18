package com.wemeet.flowfixassistant.user.presentation.dto

data class SignUpRequest(
    val username: String,
    val displayName: String? = null,
)

data class LoginRequest(
    val username: String,
)

data class TokenResponse(
    val accessToken: String,
    val username: String,
    val role: String,
)
