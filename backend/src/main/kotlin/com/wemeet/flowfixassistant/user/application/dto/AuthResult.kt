package com.wemeet.flowfixassistant.user.application.dto

data class AuthResult(
    val accessToken: String,
    val username: String,
    val role: String,
)
