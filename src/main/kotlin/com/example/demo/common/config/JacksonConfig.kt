package com.example.demo.common.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Jackson 설정
 * JSON 직렬화/역직렬화 설정을 정의합니다.
 */
@Configuration
class JacksonConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().apply {
            // Kotlin 모듈 등록
            registerModule(kotlinModule())

            // Java 8 날짜/시간 모듈 등록
            registerModule(JavaTimeModule())

            // 날짜를 타임스탬프가 아닌 ISO-8601 형식으로 직렬화
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

            // 알 수 없는 속성이 있어도 실패하지 않음
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

            // null 값은 직렬화하지 않음
            // setSerializationInclusion(JsonInclude.Include.NON_NULL)

            // Pretty print (개발 환경에서만 사용 권장)
            // enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
}
