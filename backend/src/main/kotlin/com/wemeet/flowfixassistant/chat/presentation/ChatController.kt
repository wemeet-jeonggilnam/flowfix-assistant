package com.wemeet.flowfixassistant.chat.presentation

import com.wemeet.flowfixassistant.common.presentation.ApiResponse
import com.wemeet.flowfixassistant.common.infrastructure.security.UserPrincipal
import com.wemeet.flowfixassistant.chat.presentation.dto.*
import com.wemeet.flowfixassistant.chat.application.ChatService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService,
) {
    @PostMapping
    fun chat(
        @RequestBody request: ChatSendRequest,
        @AuthenticationPrincipal principal: UserPrincipal,
    ): ApiResponse<ChatSendResponse> {
        return ApiResponse.ok(chatService.chat(request, principal.user.id))
    }

    @GetMapping("/conversations")
    fun getConversations(
        @AuthenticationPrincipal principal: UserPrincipal,
    ): ApiResponse<List<ConversationListResponse>> {
        return ApiResponse.ok(chatService.getConversations(principal.user.id))
    }

    @GetMapping("/conversations/{conversationId}/messages")
    fun getMessages(
        @PathVariable conversationId: Long,
    ): ApiResponse<List<MessageListResponse>> {
        return ApiResponse.ok(chatService.getMessages(conversationId))
    }
}
