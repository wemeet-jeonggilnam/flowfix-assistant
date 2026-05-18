package com.wemeet.flowfixassistant.user.application

import com.wemeet.flowfixassistant.user.application.dto.AuthResult
import com.wemeet.flowfixassistant.user.application.dto.LoginCommand
import com.wemeet.flowfixassistant.user.application.dto.SignUpCommand
import com.wemeet.flowfixassistant.user.domain.model.AssistantUser
import com.wemeet.flowfixassistant.user.domain.repository.AssistantUserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: AssistantUserRepository,
    private val tokenProvider: TokenProvider,
) {
    /**
     * 신규 사용자를 등록하고 인증 토큰을 발급합니다.
     *
     * @param command 회원가입 요청 정보 (사용자명, 표시명)
     * @return 인증 결과 (토큰, 사용자 정보)
     * @throws IllegalArgumentException 이미 존재하는 사용자명인 경우
     */
    fun signUp(command: SignUpCommand): AuthResult {
        require(userRepository.findByUsername(command.username) == null) {
            "이미 존재하는 사용자명입니다: ${command.username}"
        }
        val user = userRepository.save(
            AssistantUser(
                username = command.username,
                displayName = command.displayName ?: command.username,
            )
        )
        return toAuthResult(user)
    }

    /**
     * 기존 사용자를 인증하고 토큰을 발급합니다.
     *
     * @param command 로그인 요청 정보 (사용자명)
     * @return 인증 결과 (토큰, 사용자 정보)
     * @throws IllegalArgumentException 존재하지 않는 사용자인 경우
     */
    fun login(command: LoginCommand): AuthResult {
        val user = userRepository.findByUsername(command.username)
            ?: throw IllegalArgumentException("존재하지 않는 사용자입니다: ${command.username}")
        return toAuthResult(user)
    }

    private fun toAuthResult(user: AssistantUser): AuthResult {
        return AuthResult(
            accessToken = tokenProvider.generateToken(user.username, user.role),
            username = user.username,
            role = user.role,
        )
    }
}