package com.wemeet.flowfixassistant.chat.application

import com.wemeet.flowfixassistant.chat.presentation.dto.*
import com.wemeet.flowfixassistant.chat.domain.model.*
import com.wemeet.flowfixassistant.chat.domain.repository.ConversationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChatService(
    private val conversationRepository: ConversationRepository,
    private val tokenUsageService: TokenUsageService,
    private val ragClient: RagClient,
) {

    fun chat(request: ChatSendRequest, userId: Long): ChatSendResponse {
        val conversation = getOrCreateConversation(request.conversationId, userId)

        // 사용자 메시지 저장 (dirty checking)
        conversation.addMessage(ChatMessage.ofUser(conversation, request.message))

        // 대화 이력 조회 (최근 10개)
        val history = conversation.recentMessages()
            .map { MessageDto(role = it.role.name, content = it.content) }

        // RAG Engine 호출
        val ragResponse = ragClient.query(
            RagRequest(query = request.message, conversationHistory = history)
        )

        // AI 응답 메시지 저장 (dirty checking)
        val aiMessage = ChatMessage.ofAssistant(
            conversation = conversation,
            content = ragResponse.answer,
            tokenUsage = ragResponse.tokenUsage?.toTokenUsageInfo(),
        )
        conversation.addMessage(aiMessage)

        // 출처 저장 (dirty checking)
        aiMessage.addSources(ragResponse.toMessageSources(aiMessage))

        // 토큰 사용량 기록 (REQUIRES_NEW)
        tokenUsageService.record(userId, ragResponse.tokenUsage)

        return ChatSendResponse.of(conversation, aiMessage, ragResponse.tokenUsage)
    }

    @Transactional(readOnly = true)
    fun getConversations(userId: Long): List<ConversationListResponse> {
        return conversationRepository.findByUserIdOrderByUpdatedAtDesc(userId).map {
            ConversationListResponse(id = it.id, title = it.title, updatedAt = it.updatedAt)
        }
    }

    @Transactional(readOnly = true)
    fun getMessages(conversationId: Long): List<MessageListResponse> {
        val conversation = conversationRepository.findById(conversationId).orElseThrow {
            IllegalArgumentException("대화를 찾을 수 없습니다: $conversationId")
        }
        return conversation.messages.map { msg ->
            MessageListResponse(
                id = msg.id,
                role = msg.role.name,
                content = msg.content,
                sources = msg.sources.map {
                    SourceInfo(it.sourceType, it.sourceName, it.sourceSection, it.relevanceScore, it.snippet)
                },
                createdAt = msg.createdAt,
            )
        }
    }

    private fun getOrCreateConversation(conversationId: Long?, userId: Long): Conversation {
        if (conversationId != null) {
            return conversationRepository.findById(conversationId).orElseThrow {
                IllegalArgumentException("대화를 찾을 수 없습니다: $conversationId")
            }
        }
        return conversationRepository.save(Conversation(userId = userId))
    }
}
