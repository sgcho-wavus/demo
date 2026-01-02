package com.example.demo.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorMessage {
	private final String code;
    private final String message;
    
    public static ErrorMessage from(ErrorCode errorCode) {
    	return new ErrorMessage(errorCode.code(), errorCode.message());
    }
    public static ErrorMessage from(ErrorCode errorCode, String customErrorMessage) {
    	return new ErrorMessage(errorCode.code(), customErrorMessage);
    }
    
}