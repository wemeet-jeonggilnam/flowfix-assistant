package com.wemeet.flowfixassistant.chat.presentation

import com.wemeet.flowfixassistant.chat.application.service.ChatService
import com.wemeet.flowfixassistant.chat.presentation.dto.*
import com.wemeet.flowfixassistant.common.infrastructure.security.UserPrincipal
import com.wemeet.flowfixassistant.common.presentation.ApiResponse
import com.wemeet.flowfixassistant.common.presentation.toSuccessResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat/conversations")
class ChatController(
    private val chatService: ChatService,
) {
    @PostMapping
    fun createConversation(
        @Valid @RequestBody request: ChatSendRequest,
        @AuthenticationPrincipal principal: UserPrincipal,
    ): ResponseEntity<ApiResponse<ChatSendResponse>> {
        return ChatSendResponse.of(
            chatService.createConversation(request.toCommand(), principal.user.id)
        ).toSuccessResponse(HttpStatus.CREATED)
    }

    @PostMapping("/{conversationId}/messages")
    fun sendMessage(
        @PathVariable conversationId: Long,
        @Valid @RequestBody request: ChatSendRequest,
        @AuthenticationPrincipal principal: UserPrincipal,
    ): ResponseEntity<ApiResponse<ChatSendResponse>> {
        return ChatSendResponse.of(
            chatService.sendMessage(conversationId, request.toCommand(), principal.user.id)
        ).toSuccessResponse()
    }

    @GetMapping
    fun getConversations(
        @AuthenticationPrincipal principal: UserPrincipal,
    ): ResponseEntity<ApiResponse<ConversationListResponse>> {
        return ConversationListResponse.from(
            chatService.getConversations(principal.user.id)
        ).toSuccessResponse()
    }

    @GetMapping("/{conversationId}/messages")
    fun getMessages(
        @PathVariable conversationId: Long,
    ): ResponseEntity<ApiResponse<MessageListResponse>> {
        return MessageListResponse.from(
            chatService.getMessages(conversationId)
        ).toSuccessResponse()
    }
}
