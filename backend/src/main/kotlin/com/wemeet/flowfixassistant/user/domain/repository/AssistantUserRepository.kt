package com.wemeet.flowfixassistant.user.domain.repository

import com.wemeet.flowfixassistant.user.domain.model.AssistantUser

interface AssistantUserRepository {
    fun save(user: AssistantUser): AssistantUser
    fun findByUsername(username: String): AssistantUser?
}
