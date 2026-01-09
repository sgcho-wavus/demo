package com.example.demo.common.aop

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * 컨트롤러 로깅 AOP
 * 요청/응답 정보와 실행 시간을 로깅합니다.
 */
@Aspect
@Component
class LoggingAspect(
    private val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(LoggingAspect::class.java)

    /**
     * 컨트롤러의 모든 메서드를 대상으로 하는 포인트컷
     */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    fun restController() {}

    /**
     * 요청/응답 로깅 및 실행 시간 측정
     */
    @Around("restController()")
    fun logAround(joinPoint: ProceedingJoinPoint): Any? {
        val startTime = System.currentTimeMillis()

        val request = getCurrentRequest()
        val className = joinPoint.signature.declaringTypeName
        val methodName = joinPoint.signature.name
        val args = joinPoint.args

        // 요청 로깅
        logRequest(request, className, methodName, args)

        return try {
            // 메서드 실행
            val result = joinPoint.proceed()

            // 응답 로깅
            val executionTime = System.currentTimeMillis() - startTime
            logResponse(className, methodName, result, executionTime)

            result
        } catch (e: Exception) {
            // 예외 로깅
            val executionTime = System.currentTimeMillis() - startTime
            logException(className, methodName, e, executionTime)
            throw e
        }
    }

    /**
     * 요청 정보 로깅
     */
    private fun logRequest(
        request: HttpServletRequest?,
        className: String,
        methodName: String,
        args: Array<Any>
    ) {
        if (request == null) {
            logger.info("==> Request: {}.{}", className, methodName)
            return
        }

        val requestInfo = buildString {
            append("\n")
            append("=================================================\n")
            append("REQUEST: ${request.method} ${request.requestURI}\n")
            append("Handler: $className.$methodName\n")
            append("IP: ${request.remoteAddr}\n")

            if (args.isNotEmpty()) {
                append("Parameters: ${formatArgs(args)}\n")
            }

            request.queryString?.let {
                append("Query String: $it\n")
            }

            append("=================================================")
        }

        logger.info(requestInfo)
    }

    /**
     * 응답 정보 로깅
     */
    private fun logResponse(
        className: String,
        methodName: String,
        result: Any?,
        executionTime: Long
    ) {
        val responseInfo = buildString {
            append("\n")
            append("=================================================\n")
            append("RESPONSE: $className.$methodName\n")
            append("Execution Time: ${executionTime}ms\n")

            result?.let {
                append("Response: ${formatResult(it)}\n")
            }

            append("=================================================")
        }

        logger.info(responseInfo)
    }

    /**
     * 예외 정보 로깅
     */
    private fun logException(
        className: String,
        methodName: String,
        exception: Exception,
        executionTime: Long
    ) {
        val errorInfo = buildString {
            append("\n")
            append("=================================================\n")
            append("EXCEPTION: $className.$methodName\n")
            append("Execution Time: ${executionTime}ms\n")
            append("Exception: ${exception.javaClass.simpleName}\n")
            append("Message: ${exception.message}\n")
            append("=================================================")
        }

        logger.error(errorInfo, exception)
    }

    /**
     * 인자를 JSON 형식으로 포맷팅
     */
    private fun formatArgs(args: Array<Any>): String {
        return try {
            args.joinToString(", ") { arg ->
                when (arg) {
                    is HttpServletRequest -> "[HttpServletRequest]"
                    else -> objectMapper.writeValueAsString(arg)
                }
            }
        } catch (e: Exception) {
            args.contentToString()
        }
    }

    /**
     * 결과를 JSON 형식으로 포맷팅
     */
    private fun formatResult(result: Any): String {
        return try {
            objectMapper.writeValueAsString(result)
        } catch (e: Exception) {
            result.toString()
        }
    }

    /**
     * 현재 HTTP 요청 객체 가져오기
     */
    private fun getCurrentRequest(): HttpServletRequest? {
        return try {
            val attributes = RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes
            attributes.request
        } catch (e: IllegalStateException) {
            null
        }
    }
}
