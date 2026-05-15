package com.wemeet.flowfixassistant.domain.user

import jakarta.persistence.*
import java.time.LocalDateTime

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

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
