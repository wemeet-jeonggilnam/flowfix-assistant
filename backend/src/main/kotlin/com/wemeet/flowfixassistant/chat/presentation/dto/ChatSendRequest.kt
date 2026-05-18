package com.wemeet.flowfixassistant.chat.presentation.dto

import com.wemeet.flowfixassistant.chat.application.dto.ChatSendCommand
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ChatSendRequest(
    @field:NotBlank(message = "메시지는 필수입니다.")
    @field:Size(max = 5000, message = "메시지는 5000자 이내로 입력해주세요.")
    val message: String,
) {
    fun toCommand(): ChatSendCommand {
        return ChatSendCommand(message = message)
    }
}
