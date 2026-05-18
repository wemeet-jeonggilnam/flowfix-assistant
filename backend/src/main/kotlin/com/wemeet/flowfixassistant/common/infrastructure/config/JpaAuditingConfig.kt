package com.wemeet.flowfixassistant.common.infrastructure.config

import com.wemeet.flowfixassistant.user.infrastructure.security.UserPrincipal
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import java.util.Optional

@Configuration
@EnableJpaAuditing
class JpaAuditingConfig {

    @Bean
    fun auditorAware(): AuditorAware<String> {
        return AuditorAware {
            val authentication = SecurityContextHolder.getContext().authentication
            if (authentication != null && authentication.principal is UserPrincipal) {
                Optional.of((authentication.principal as UserPrincipal).username)
            } else {
                Optional.of("system")
            }
        }
    }
}
