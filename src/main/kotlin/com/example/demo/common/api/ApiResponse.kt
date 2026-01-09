package com.example.demo.common.api

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

/**
 * API 응답 표준 형식
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ErrorInfo? = null,
    val timestamp: LocalDateTime = LocalDateTime.now()
) {
    companion object {
        /**
         * 성공 응답 생성
         */
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(
                success = true,
                data = data,
                error = null
            )
        }

        /**
         * 데이터 없는 성공 응답
         */
        fun success(): ApiResponse<Unit> {
            return ApiResponse(
                success = true,
                data = null,
                error = null
            )
        }

        /**
         * 실패 응답 생성
         */
        fun <T> error(code: String, message: String): ApiResponse<T> {
            return ApiResponse(
                success = false,
                data = null,
                error = ErrorInfo(code, message)
            )
        }

        /**
         * 실패 응답 생성 (ErrorInfo 객체 사용)
         */
        fun <T> error(errorInfo: ErrorInfo): ApiResponse<T> {
            return ApiResponse(
                success = false,
                data = null,
                error = errorInfo
            )
        }
    }

    /**
     * 에러 정보
     */
    data class ErrorInfo(
        val code: String,
        val message: String
    )
}
