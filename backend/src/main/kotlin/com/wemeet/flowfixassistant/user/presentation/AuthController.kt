package com.wemeet.flowfixassistant.user.presentation

import com.wemeet.flowfixassistant.common.infrastructure.security.JwtTokenProvider
import com.wemeet.flowfixassistant.common.presentation.ApiResponse
import com.wemeet.flowfixassistant.user.application.UserService
import com.wemeet.flowfixassistant.user.presentation.dto.LoginRequest
import com.wemeet.flowfixassistant.user.presentation.dto.SignUpRequest
import com.wemeet.flowfixassistant.user.presentation.dto.TokenResponse
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
    fun signUp(@RequestBody request: SignUpRequest): ApiResponse<TokenResponse> {
        val user = userService.getOrCreateUser(request.username, request.displayName)
        val token = jwtTokenProvider.generateToken(user.username, user.role)
        return ApiResponse.ok(TokenResponse(accessToken = token, username = user.username, role = user.role))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ApiResponse<TokenResponse> {
        // UserDetailsService로 DB 검증
        userDetailsService.loadUserByUsername(request.username)
        val user = userService.getOrCreateUser(request.username)
        val token = jwtTokenProvider.generateToken(user.username, user.role)
        return ApiResponse.ok(TokenResponse(accessToken = token, username = user.username, role = user.role))
    }
}
