package com.wemeet.flowfixassistant.user.application

interface TokenProvider {
    fun generateToken(username: String, role: String): String
    fun getUsername(token: String): String
    fun isValid(token: String): Boolean
}