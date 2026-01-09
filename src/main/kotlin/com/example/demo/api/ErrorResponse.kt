package com.example.demo.api

import java.time.LocalDateTime

data class ErrorResponse(
    val errorCode: String,
    val message: String,
    val timestamp: LocalDateTime
) {
    companion object {
        fun from(errorMessage: ErrorMessage): ErrorResponse {
            return ErrorResponse(
                errorCode = errorMessage.code,
                message = errorMessage.message,
                timestamp = LocalDateTime.now()
            )
        }
    }
}
