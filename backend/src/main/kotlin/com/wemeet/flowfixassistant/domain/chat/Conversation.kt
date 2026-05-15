package com.wemeet.flowfixassistant.domain.chat

import com.wemeet.flowfixassistant.domain.user.AssistantUser
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "conversation")
class Conversation(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: AssistantUser,

    @Column(length = 500)
    var title: String? = null,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)
