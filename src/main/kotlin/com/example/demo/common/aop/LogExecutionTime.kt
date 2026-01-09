package com.example.demo.common.aop

/**
 * 메서드 실행 시간을 로깅하는 어노테이션
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LogExecutionTime
