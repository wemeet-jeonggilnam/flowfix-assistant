package com.wemeet.flowfixassistant.user.infrastructure

import com.wemeet.flowfixassistant.user.domain.model.AssistantUser
import com.wemeet.flowfixassistant.user.domain.repository.AssistantUserRepository
import org.springframework.data.jpa.repository.JpaRepository

interface JpaAssistantUserRepository : AssistantUserRepository, JpaRepository<AssistantUser, Long>
