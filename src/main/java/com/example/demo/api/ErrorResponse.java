package com.example.demo.api;

import java.time.LocalDateTime;

public record ErrorResponse(
    String errorCode,
    String message,
    LocalDateTime timestamp
) {

	public static ErrorResponse from(ErrorMessage errorMessage) {
        return new ErrorResponse(
            errorMessage.getCode(),
            errorMessage.getMessage(),
            LocalDateTime.now()
        );
    }
}
