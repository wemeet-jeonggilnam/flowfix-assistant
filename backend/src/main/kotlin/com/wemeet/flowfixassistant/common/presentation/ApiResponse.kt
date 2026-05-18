package com.wemeet.flowfixassistant.common.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null,
) {
    companion object {
        fun <T> ok(data: T): ResponseEntity<ApiResponse<T>> =
            ResponseEntity.ok(ApiResponse(success = true, data = data))

        fun <T> created(data: T): ResponseEntity<ApiResponse<T>> =
            ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse(success = true, data = data))

        fun <T> error(status: HttpStatus, message: String): ResponseEntity<ApiResponse<T>> =
            ResponseEntity.status(status).body(ApiResponse(success = false, error = message))
    }
}
