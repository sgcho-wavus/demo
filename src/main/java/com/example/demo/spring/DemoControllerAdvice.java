package com.example.demo.spring;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.api.ErrorCode;
import com.example.demo.api.ErrorMessage;
import com.example.demo.api.ErrorResponse;
import com.example.demo.exception.DemoBizException;
import com.example.demo.exception.DemoException;

@RestControllerAdvice
public class DemoControllerAdvice {

    @ExceptionHandler(DemoBizException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(DemoException e) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.from(e.getErrorMessage()));
    }
}