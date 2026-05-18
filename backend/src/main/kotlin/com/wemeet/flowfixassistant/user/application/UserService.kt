package com.wemeet.flowfixassistant.user.application

import com.wemeet.flowfixassistant.user.domain.model.AssistantUser
import com.wemeet.flowfixassistant.user.domain.repository.AssistantUserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: AssistantUserRepository,
) {
    fun getOrCreateUser(username: String, displayName: String? = null): AssistantUser {
        return userRepository.findByUsername(username)
            ?: userRepository.save(
                AssistantUser(
                    username = username,
                    displayName = displayName ?: username,
                )
            )
    }
}
