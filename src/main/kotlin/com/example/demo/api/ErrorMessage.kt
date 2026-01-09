package com.example.demo.api

data class ErrorMessage(
    val code: String,
    val message: String
) {
    companion object {
        fun from(errorCode: ErrorCode, customErrorMessage: String? = null): ErrorMessage {
            return ErrorMessage(
                code = errorCode.code,
                message = customErrorMessage ?: errorCode.message
            )
        }
    }
}
