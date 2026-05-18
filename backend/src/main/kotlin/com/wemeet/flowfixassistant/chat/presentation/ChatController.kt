package com.wemeet.flowfixassistant.chat.presentation

import com.wemeet.flowfixassistant.chat.application.service.ChatService
import com.wemeet.flowfixassistant.chat.presentation.dto.*
import com.wemeet.flowfixassistant.common.infrastructure.security.UserPrincipal
import com.wemeet.flowfixassistant.common.presentation.ApiResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService,
) {
    @PostMapping
    fun chat(
        @Valid @RequestBody request: ChatSendRequest,
        @AuthenticationPrincipal principal: UserPrincipal,
    ): ResponseEntity<ApiResponse<ChatSendResponse>> {
        val result = chatService.chat(request.toCommand(), principal.user.id)
        return ApiResponse.ok(ChatSendResponse.of(result))
    }

    @GetMapping("/conversations")
    fun getConversations(
        @AuthenticationPrincipal principal: UserPrincipal,
    ): ResponseEntity<ApiResponse<ConversationListResponse>> {
        val result = chatService.getConversations(principal.user.id)
        return ApiResponse.ok(ConversationListResponse.from(result))
    }

    @GetMapping("/conversations/{conversationId}/messages")
    fun getMessages(
        @PathVariable conversationId: Long,
    ): ResponseEntity<ApiResponse<MessageListResponse>> {
        val result = chatService.getMessages(conversationId)
        return ApiResponse.ok(MessageListResponse.from(result))
    }
}
