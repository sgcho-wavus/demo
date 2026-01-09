package com.example.demo.common.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * @LogExecutionTime 어노테이션이 붙은 메서드의 실행 시간을 측정하는 AOP
 */
@Aspect
@Component
class ExecutionTimeAspect {

    private val logger = LoggerFactory.getLogger(ExecutionTimeAspect::class.java)

    /**
     * @LogExecutionTime 어노테이션이 붙은 메서드의 실행 시간 측정
     */
    @Around("@annotation(com.example.demo.common.aop.LogExecutionTime)")
    fun logExecutionTime(joinPoint: ProceedingJoinPoint): Any? {
        val startTime = System.currentTimeMillis()

        return try {
            joinPoint.proceed()
        } finally {
            val executionTime = System.currentTimeMillis() - startTime
            val className = joinPoint.signature.declaringTypeName
            val methodName = joinPoint.signature.name

            logger.info("{}#{} executed in {}ms", className, methodName, executionTime)
        }
    }
}
