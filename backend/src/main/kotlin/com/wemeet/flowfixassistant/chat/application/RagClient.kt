package com.wemeet.flowfixassistant.chat.application

import com.wemeet.flowfixassistant.chat.presentation.dto.RagRequest
import com.wemeet.flowfixassistant.chat.presentation.dto.RagResponse

interface RagClient {
    fun query(request: RagRequest): RagResponse
}
