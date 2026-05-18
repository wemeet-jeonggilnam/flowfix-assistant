package com.wemeet.flowfixassistant.user.domain.model

import com.wemeet.flowfixassistant.common.domain.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "assistant_user")
class AssistantUser(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true, length = 100)
    val username: String,

    @Column(name = "display_name", nullable = false, length = 200)
    var displayName: String,

    @Column(nullable = false, length = 50)
    var role: String = "USER",
) : BaseEntity()
