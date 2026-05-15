package com.wemeet.flowfixassistant.infrastructure.user

import com.wemeet.flowfixassistant.domain.user.AssistantUser
import org.springframework.data.jpa.repository.JpaRepository

interface AssistantUserRepository : JpaRepository<AssistantUser, Long> {
    fun findByUsername(username: String): AssistantUser?
}
