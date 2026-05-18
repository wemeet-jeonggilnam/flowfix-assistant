package com.wemeet.flowfixassistant.common.presentation

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null,
) {
    companion object {
        fun <T> error(status: HttpStatus, message: String): ResponseEntity<ApiResponse<T>> =
            ResponseEntity.status(status).body(ApiResponse(success = false, error = message))
    }
}

data class ListResponse<T>(
    val items: List<T>,
)

/**
 * 단일 객체 성공 응답을 위한 확장 함수
 *
 * @param T 응답 데이터의 타입
 * @param status HTTP 상태 코드 (기본값: 200 OK)
 * @receiver 응답으로 반환할 데이터 (nullable)
 * @return 표준 ApiResponse 구조가 포함된 ResponseEntity
 */
fun <T> T?.toSuccessResponse(
    status: HttpStatus = HttpStatus.OK,
): ResponseEntity<ApiResponse<T>> =
    ResponseEntity.status(status).body(ApiResponse(success = true, data = this))

/**
 * List 컬렉션을 위한 전용 성공 응답 확장 함수
 *
 * @param T List 항목의 데이터 타입
 * @param status HTTP 상태 코드 (기본값: 200 OK)
 * @receiver 응답으로 반환할 List 데이터
 * @return items 키로 래핑된 List가 포함된 표준 ApiResponse 구조의 ResponseEntity
 */
fun <T> List<T>.toSuccessResponse(
    status: HttpStatus = HttpStatus.OK,
): ResponseEntity<ApiResponse<ListResponse<T>>> =
    ResponseEntity.status(status).body(ApiResponse(success = true, data = ListResponse(items = this)))
