package com.wemeet.flowfixassistant.chat.domain.model

import com.wemeet.flowfixassistant.common.domain.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "conversation")
class Conversation(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(length = 500)
    var title: String? = null,

    @OneToMany(mappedBy = "conversation", cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderBy("createdAt ASC")
    val messages: MutableList<ChatMessage> = mutableListOf(),
) : BaseEntity() {
    fun addMessage(message: ChatMessage) {
        messages.add(message)
        message.conversation = this
        initTitleIfAbsent(message.content)
    }

    private fun initTitleIfAbsent(content: String) {
        if (title == null) {
            title = content.take(100)
        }
    }

    fun recentMessages(limit: Int = 10): List<ChatMessage> {
        return messages.takeLast(limit)
    }
}
