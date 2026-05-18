package com.wemeet.flowfixassistant.conversation.application

import com.wemeet.flowfixassistant.conversation.application.dto.RagRequest
import com.wemeet.flowfixassistant.conversation.application.dto.RagResponse

interface RagClient {
    fun query(request: RagRequest): RagResponse
}
