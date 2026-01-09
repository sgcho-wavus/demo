package com.example.demo.api

enum class ErrorCode(
    val code: String,
    val message: String
) {
    SURVEY_NOT_FOUND(
        "SURVEY_NOT_FOUND",
        "설문을 찾을 수 없습니다"
    ),

    SURVEY_CLOSED(
        "SURVEY_CLOSED",
        "이미 종료된 설문입니다"
    ),

    BAD_REQUEST(
        "BAD_REQUEST",
        "잘못된 요청입니다."
    ),

    // 공통 에러 코드
    INVALID_INPUT_VALUE(
        "INVALID_INPUT_VALUE",
        "입력값이 올바르지 않습니다."
    ),

    NOT_FOUND(
        "NOT_FOUND",
        "요청한 리소스를 찾을 수 없습니다."
    ),

    INTERNAL_SERVER_ERROR(
        "INTERNAL_SERVER_ERROR",
        "서버 내부 오류가 발생했습니다."
    ),

    // 인증 관련 에러 코드
    DUPLICATE_USERNAME(
        "DUPLICATE_USERNAME",
        "이미 사용 중인 사용자 이름입니다."
    ),

    DUPLICATE_EMAIL(
        "DUPLICATE_EMAIL",
        "이미 사용 중인 이메일입니다."
    ),

    INVALID_TOKEN(
        "INVALID_TOKEN",
        "유효하지 않은 토큰입니다."
    ),

    USER_NOT_FOUND(
        "USER_NOT_FOUND",
        "사용자를 찾을 수 없습니다."
    );

    fun toErrorMessage(customMessage: String? = null): ErrorMessage {
        return ErrorMessage.from(this, customMessage)
    }
}
