package com.example.demo.common.exception

import com.example.demo.api.ErrorCode
import com.example.demo.api.ErrorResponse
import com.example.demo.exception.DemoBizException
import com.example.demo.exception.DemoException
import com.example.demo.exception.DemoSystemException
import com.example.demo.exception.DemoValidationException
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException

/**
 * 전역 예외 처리 핸들러
 * 모든 컨트롤러에서 발생하는 예외를 일관되게 처리합니다.
 */
@RestControllerAdvice
class RestExceptionHandler {

    private val logger = LoggerFactory.getLogger(RestExceptionHandler::class.java)

    /**
     * 비즈니스 예외 처리
     */
    @ExceptionHandler(DemoBizException::class)
    fun handleBusinessException(ex: DemoException): ResponseEntity<ErrorResponse> {
        logger.warn("Business exception occurred: {}", ex.errorMessage.message)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.from(ex.errorMessage))
    }

    /**
     * 시스템 예외 처리
     */
    @ExceptionHandler(DemoSystemException::class)
    fun handleSystemException(ex: DemoException): ResponseEntity<ErrorResponse> {
        logger.error("System exception occurred: {}", ex.errorMessage.message, ex)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.from(ex.errorMessage))
    }

    /**
     * 검증 예외 처리
     */
    @ExceptionHandler(DemoValidationException::class)
    fun handleValidationException(ex: DemoException): ResponseEntity<ErrorResponse> {
        logger.warn("Validation exception occurred: {}", ex.errorMessage.message)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.from(ex.errorMessage))
    }

    /**
     * Spring Validation 예외 처리 (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.fieldErrors
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }

        logger.warn("Validation failed: {}", errors)

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.from(ErrorCode.INVALID_INPUT_VALUE.toErrorMessage(errors)))
    }

    /**
     * Constraint Violation 예외 처리
     */
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        val errors = ex.constraintViolations
            .joinToString(", ") { "${it.propertyPath}: ${it.message}" }

        logger.warn("Constraint violation: {}", errors)

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.from(ErrorCode.INVALID_INPUT_VALUE.toErrorMessage(errors)))
    }

    /**
     * 타입 불일치 예외 처리
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> {
        val error = "${ex.name} should be of type ${ex.requiredType?.name}"
        logger.warn("Type mismatch: {}", error)

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.from(ErrorCode.INVALID_INPUT_VALUE.toErrorMessage(error)))
    }

    /**
     * 404 예외 처리
     */
    @ExceptionHandler(NoHandlerFoundException::class)
    fun handleNoHandlerFound(ex: NoHandlerFoundException): ResponseEntity<ErrorResponse> {
        logger.warn("No handler found: {} {}", ex.httpMethod, ex.requestURL)

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponse.from(ErrorCode.NOT_FOUND.toErrorMessage()))
    }

    /**
     * IllegalArgumentException 처리
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        logger.warn("Illegal argument: {}", ex.message)

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponse.from(ErrorCode.INVALID_INPUT_VALUE.toErrorMessage(ex.message ?: "Invalid argument")))
    }

    /**
     * 기타 모든 예외 처리
     */
    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ErrorResponse> {
        logger.error("Unexpected exception occurred: {}", ex.message, ex)

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR.toErrorMessage()))
    }
}
