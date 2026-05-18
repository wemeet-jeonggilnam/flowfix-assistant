package com.wemeet.flowfixassistant.user.application.dto

data class SignUpCommand(
    val username: String,
    val displayName: String? = null,
)

data class LoginCommand(
    val username: String,
)