package com.example.demo.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.util.*

/**
 * JPA Auditing 설정
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
class JpaAuditingConfig {

    /**
     * AuditorAware 빈 등록
     * 현재 사용자 정보를 제공합니다.
     */
    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditorAware {
            // TODO: Spring Security 연동 시 SecurityContextHolder에서 사용자 정보 가져오기
            // val authentication = SecurityContextHolder.getContext().authentication
            // return Optional.ofNullable(authentication?.name ?: "system")

            // 현재는 기본값 "system" 반환
            Optional.of("system")
        }
    }
}
