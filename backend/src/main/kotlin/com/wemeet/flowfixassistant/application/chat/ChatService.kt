package com.wemeet.flowfixassistant.application.chat

import com.wemeet.flowfixassistant.presentation.chat.dto.*
import com.wemeet.flowfixassistant.domain.chat.*
import com.wemeet.flowfixassistant.infrastructure.chat.*
import com.wemeet.flowfixassistant.domain.user.AssistantUser
import com.wemeet.flowfixassistant.infrastructure.user.AssistantUserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional
class ChatService(
    private val conversationRepository: ConversationRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val messageSourceRepository: MessageSourceRepository,
    private val tokenUsageLogRepository: TokenUsageLogRepository,
    private val userRepository: AssistantUserRepository,
    private val ragClientService: RagClientService,
) {

    fun chat(request: ChatRequest, username: String): ChatResponse {
        val user = getOrCreateUser(username)
        val conversation = getOrCreateConversation(request.conversationId, user, request.message)

        // 사용자 메시지 저장
        chatMessageRepository.save(
            ChatMessage(conversation = conversation, role = "user", content = request.message)
        )

        // 대화 이력 조회 (최근 10개)
        val history = chatMessageRepository
            .findByConversationIdOrderByCreatedAtAsc(conversation.id)
            .takeLast(10)
            .map { MessageDto(role = it.role, content = it.content) }

        // RAG Engine 호출
        val ragResponse = ragClientService.query(
            RagRequest(query = request.message, conversationHistory = history)
        )

        // AI 응답 메시지 저장
        val aiMessage = chatMessageRepository.save(
            ChatMessage(
                conversation = conversation,
                role = "assistant",
                content = ragResponse.answer,
                inputTokens = ragResponse.tokenUsage?.inputTokens,
                outputTokens = ragResponse.tokenUsage?.outputTokens,
            )
        )

        // 출처 저장
        val sources = ragResponse.sources.map { src ->
            messageSourceRepository.save(
                MessageSource(
                    message = aiMessage,
                    sourceType = src.type,
                    sourceName = src.name,
                    sourceSection = src.section,
                    relevanceScore = src.relevanceScore,
                    snippet = src.snippet,
                )
            )
        }

        // 토큰 사용량 기록
        ragResponse.tokenUsage?.let { usage ->
            tokenUsageLogRepository.save(
                TokenUsageLog(
                    user = user,
                    model = usage.model,
                    inputTokens = usage.inputTokens,
                    outputTokens = usage.outputTokens,
                )
            )
        }

        // 대화 제목 자동 설정 (첫 메시지)
        if (conversation.title == null) {
            conversation.title = request.message.take(100)
            conversation.updatedAt = LocalDateTime.now()
        }

        return ChatResponse(
            conversationId = conversation.id,
            messageId = aiMessage.id,
            answer = ragResponse.answer,
            sources = sources.map {
                SourceInfo(
                    type = it.sourceType,
                    name = it.sourceName,
                    section = it.sourceSection,
                    relevanceScore = it.relevanceScore,
                    snippet = it.snippet,
                )
            },
            tokenUsage = ragResponse.tokenUsage?.let { TokenUsage(it.inputTokens, it.outputTokens) },
            createdAt = aiMessage.createdAt,
        )
    }

    @Transactional(readOnly = true)
    fun getConversations(username: String): List<ConversationSummary> {
        val user = userRepository.findByUsername(username) ?: return emptyList()
        return conversationRepository.findByUserIdOrderByUpdatedAtDesc(user.id).map {
            ConversationSummary(id = it.id, title = it.title, updatedAt = it.updatedAt)
        }
    }

    @Transactional(readOnly = true)
    fun getMessages(conversationId: Long): List<MessageWithSources> {
        return chatMessageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId).map { msg ->
            val sources = messageSourceRepository.findByMessageId(msg.id)
            MessageWithSources(
                id = msg.id,
                role = msg.role,
                content = msg.content,
                sources = sources.map {
                    SourceInfo(it.sourceType, it.sourceName, it.sourceSection, it.relevanceScore, it.snippet)
                },
                createdAt = msg.createdAt,
            )
        }
    }

    private fun getOrCreateUser(username: String): AssistantUser {
        return userRepository.findByUsername(username)
            ?: userRepository.save(AssistantUser(username = username, displayName = username))
    }

    private fun getOrCreateConversation(conversationId: Long?, user: AssistantUser, firstMessage: String): Conversation {
        if (conversationId != null) {
            return conversationRepository.findById(conversationId).orElseThrow {
                IllegalArgumentException("대화를 찾을 수 없습니다: $conversationId")
            }
        }
        return conversationRepository.save(Conversation(user = user))
    }
}

data class ConversationSummary(val id: Long, val title: String?, val updatedAt: LocalDateTime)

data class MessageWithSources(
    val id: Long,
    val role: String,
    val content: String,
    val sources: List<SourceInfo>,
    val createdAt: LocalDateTime,
)
