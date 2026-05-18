package com.wemeet.flowfixassistant.chat.domain.model

import com.wemeet.flowfixassistant.chat.domain.enums.ChatRole
import com.wemeet.flowfixassistant.chat.domain.vo.TokenUsageInfo
import com.wemeet.flowfixassistant.common.domain.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "chat_message")
class ChatMessage(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    var conversation: Conversation,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val role: ChatRole,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Embedded
    val tokenUsage: TokenUsageInfo? = null,

    @OneToMany(mappedBy = "message", cascade = [CascadeType.ALL], orphanRemoval = true)
    val sources: MutableList<MessageSource> = mutableListOf(),
) : BaseEntity() {
    fun addSource(source: MessageSource) {
        sources.add(source)
    }

    fun addSources(sources: List<MessageSource>) {
        this.sources.addAll(sources)
    }

    companion object {
        fun ofUser(conversation: Conversation, content: String): ChatMessage {
            return ChatMessage(conversation = conversation, role = ChatRole.USER, content = content)
        }

        fun ofAssistant(
            conversation: Conversation,
            content: String,
            tokenUsage: TokenUsageInfo? = null,
        ): ChatMessage {
            return ChatMessage(
                conversation = conversation,
                role = ChatRole.ASSISTANT,
                content = content,
                tokenUsage = tokenUsage,
            )
        }
    }
}
