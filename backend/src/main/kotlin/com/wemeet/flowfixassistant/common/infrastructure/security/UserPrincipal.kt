package com.wemeet.flowfixassistant.common.infrastructure.security

import com.wemeet.flowfixassistant.user.domain.model.AssistantUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal(
    val user: AssistantUser,
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_${user.role}"))

    override fun getPassword(): String = ""

    override fun getUsername(): String = user.username
}
