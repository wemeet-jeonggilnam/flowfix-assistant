package com.wemeet.flowfixassistant.conversation.application.service

import com.wemeet.flowfixassistant.conversation.application.RagClient
import com.wemeet.flowfixassistant.conversation.application.dto.*
import com.wemeet.flowfixassistant.conversation.domain.model.*
import com.wemeet.flowfixassistant.conversation.domain.repository.ConversationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChatService(
    private val conversationRepository: ConversationRepository,
    private val tokenUsageService: TokenUsageService,
    private val ragClient: RagClient,
) {

    /**
     * 새 대화를 생성하고 첫 메시지를 처리합니다.
     *
     * @param command 채팅 전송 명령
     * @param userId 사용자 ID
     * @return 채팅 전송 결과 (대화, AI 응답, 토큰 사용량)
     */
    fun createConversation(command: ChatSendCommand, userId: Long): ChatSendResult {
        val conversation = conversationRepository.save(Conversation(userId = userId))
        return processChat(conversation, command, userId)
    }

    /**
     * 기존 대화에 메시지를 전송하고 AI 응답을 반환합니다.
     *
     * @param conversationId 대화 ID
     * @param command 채팅 전송 명령
     * @param userId 사용자 ID
     * @return 채팅 전송 결과 (대화, AI 응답, 토큰 사용량)
     */
    fun sendMessage(conversationId: Long, command: ChatSendCommand, userId: Long): ChatSendResult {
        val conversation = findConversation(conversationId)
        return processChat(conversation, command, userId)
    }

    private fun processChat(conversation: Conversation, command: ChatSendCommand, userId: Long): ChatSendResult {
        // 사용자 메시지 저장 (dirty checking)
        conversation.addMessage(ChatMessage.ofUser(conversation, command.message))

        // 대화 이력 조회 (최근 10개)
        val history = conversation.recentMessages()
            .map { MessageDto(role = it.role.name, content = it.content) }

        // RAG Engine 호출
        val ragResponse = ragClient.query(
            RagRequest(query = command.message, conversationHistory = history)
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

        return ChatSendResult(conversation = conversation, aiMessage = aiMessage, tokenUsage = ragResponse.tokenUsage)
    }

    /**
     * 사용자의 대화 목록을 최신순으로 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 대화 목록
     */
    @Transactional(readOnly = true)
    fun getConversations(userId: Long): List<Conversation> {
        return conversationRepository.findByUserIdOrderByUpdatedAtDesc(userId)
    }

    /**
     * 대화의 메시지 목록을 조회합니다.
     *
     * @param conversationId 대화 ID
     * @return 메시지를 포함한 대화
     */
    @Transactional(readOnly = true)
    fun getMessages(conversationId: Long): Conversation {
        return findConversation(conversationId)
    }

    private fun findConversation(conversationId: Long): Conversation {
        return conversationRepository.findById(conversationId).orElseThrow {
            IllegalArgumentException("대화를 찾을 수 없습니다: $conversationId")
        }
    }
}
