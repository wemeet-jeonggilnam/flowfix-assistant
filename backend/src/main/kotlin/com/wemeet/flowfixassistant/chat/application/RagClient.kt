package com.wemeet.flowfixassistant.chat.application

import com.wemeet.flowfixassistant.chat.application.dto.RagRequest
import com.wemeet.flowfixassistant.chat.application.dto.RagResponse

interface RagClient {
    fun query(request: RagRequest): RagResponse
}
