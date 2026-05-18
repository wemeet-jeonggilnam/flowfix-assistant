package com.wemeet.flowfixassistant.user.presentation

import com.wemeet.flowfixassistant.common.infrastructure.security.JwtTokenProvider
import com.wemeet.flowfixassistant.common.presentation.ApiResponse
import com.wemeet.flowfixassistant.user.application.UserService
import com.wemeet.flowfixassistant.user.presentation.dto.LoginRequest
import com.wemeet.flowfixassistant.user.presentation.dto.SignUpRequest
import com.wemeet.flowfixassistant.user.presentation.dto.TokenResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val userDetailsService: UserDetailsService,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody request: SignUpRequest): ResponseEntity<ApiResponse<TokenResponse>> {
        val user = userService.getOrCreateUser(request.username, request.displayName)
        val token = jwtTokenProvider.generateToken(user.username, user.role)
        return ApiResponse.ok(TokenResponse.of(user, token))
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<ApiResponse<TokenResponse>> {
        userDetailsService.loadUserByUsername(request.username)
        val user = userService.getOrCreateUser(request.username)
        val token = jwtTokenProvider.generateToken(user.username, user.role)
        return ApiResponse.ok(TokenResponse.of(user, token))
    }
}
