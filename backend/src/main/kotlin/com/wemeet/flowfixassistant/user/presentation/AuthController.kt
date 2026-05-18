package com.wemeet.flowfixassistant.user.presentation

import com.wemeet.flowfixassistant.common.presentation.ApiResponse
import com.wemeet.flowfixassistant.common.presentation.toSuccessResponse
import com.wemeet.flowfixassistant.user.application.UserService
import com.wemeet.flowfixassistant.user.presentation.dto.LoginRequest
import com.wemeet.flowfixassistant.user.presentation.dto.SignUpRequest
import com.wemeet.flowfixassistant.user.presentation.dto.TokenResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
) {
    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody request: SignUpRequest): ResponseEntity<ApiResponse<TokenResponse>> {
        return TokenResponse.from(
            userService.signUp(request.toCommand())
        ).toSuccessResponse()
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<ApiResponse<TokenResponse>> {
        return TokenResponse.from(
            userService.login(request.toCommand())
        ).toSuccessResponse()
    }
}
