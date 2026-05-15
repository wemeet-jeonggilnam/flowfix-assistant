package com.wemeet.flowfixassistant.presentation.chat

import com.wemeet.flowfixassistant.presentation.common.ApiResponse
import com.wemeet.flowfixassistant.presentation.chat.dto.ChatRequest
import com.wemeet.flowfixassistant.presentation.chat.dto.ChatResponse
import com.wemeet.flowfixassistant.application.chat.ChatService
import com.wemeet.flowfixassistant.application.chat.ConversationSummary
import com.wemeet.flowfixassistant.application.chat.MessageWithSources
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService,
) {
    // MVP: username을 헤더로 전달, Phase 1 Gate 후 JWT에서 추출
    @PostMapping
    fun chat(
        @RequestBody request: ChatRequest,
        @RequestHeader("X-Username", defaultValue = "anonymous") username: String,
    ): ApiResponse<ChatResponse> {
        return ApiResponse.ok(chatService.chat(request, username))
    }

    @GetMapping("/conversations")
    fun getConversations(
        @RequestHeader("X-Username", defaultValue = "anonymous") username: String,
    ): ApiResponse<List<ConversationSummary>> {
        return ApiResponse.ok(chatService.getConversations(username))
    }

    @GetMapping("/conversations/{conversationId}/messages")
    fun getMessages(
        @PathVariable conversationId: Long,
    ): ApiResponse<List<MessageWithSources>> {
        return ApiResponse.ok(chatService.getMessages(conversationId))
    }
}
