package com.wemeet.flowfixassistant.conversation.domain.model

import com.wemeet.flowfixassistant.common.domain.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "message_source")
class MessageSource(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    val message: ChatMessage,

    @Column(name = "source_type", nullable = false, length = 50)
    val sourceType: String,

    @Column(name = "source_name", nullable = false, length = 500)
    val sourceName: String,

    @Column(name = "source_section", length = 500)
    val sourceSection: String? = null,

    @Column(name = "relevance_score")
    val relevanceScore: Double? = null,

    @Column(columnDefinition = "TEXT")
    val snippet: String? = null,
) : BaseEntity()
