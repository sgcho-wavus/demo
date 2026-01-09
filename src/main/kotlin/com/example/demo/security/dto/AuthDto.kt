package com.example.demo.security.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignUpRequest(
    @field:NotBlank(message = "사용자 이름은 필수입니다")
    @field:Size(min = 3, max = 50, message = "사용자 이름은 3~50자 사이여야 합니다")
    val username: String,

    @field:NotBlank(message = "이메일은 필수입니다")
    @field:Email(message = "올바른 이메일 형식이 아닙니다")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수입니다")
    @field:Size(min = 6, max = 100, message = "비밀번호는 6~100자 사이여야 합니다")
    val password: String
)

data class LoginRequest(
    @field:NotBlank(message = "사용자 이름은 필수입니다")
    val username: String,

    @field:NotBlank(message = "비밀번호는 필수입니다")
    val password: String
)

data class JwtResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val username: String,
    val email: String,
    val roles: Set<String>
)

data class RefreshTokenRequest(
    @field:NotBlank(message = "리프레시 토큰은 필수입니다")
    val refreshToken: String
)
