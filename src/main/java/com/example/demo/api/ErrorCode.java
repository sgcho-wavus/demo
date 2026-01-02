package com.example.demo.api;

public enum ErrorCode {

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
	)
	;
	

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() { return code; }
    public String message() { return message; }
}