package com.example.demo.security.controller

import com.example.demo.common.api.ApiResponse
import com.example.demo.security.dto.JwtResponse
import com.example.demo.security.dto.LoginRequest
import com.example.demo.security.dto.RefreshTokenRequest
import com.example.demo.security.dto.SignUpRequest
import com.example.demo.security.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    fun signUp(@Valid @RequestBody request: SignUpRequest): ApiResponse<JwtResponse> {
        val response = authService.signUp(request)
        return ApiResponse.success(response)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ApiResponse<JwtResponse> {
        val response = authService.login(request)
        return ApiResponse.success(response)
    }

    @PostMapping("/refresh")
    fun refreshToken(@Valid @RequestBody request: RefreshTokenRequest): ApiResponse<JwtResponse> {
        val response = authService.refreshAccessToken(request.refreshToken)
        return ApiResponse.success(response)
    }

    @GetMapping("/me")
    fun getCurrentUser(): ApiResponse<Map<String, Any>> {
        val authentication = org.springframework.security.core.context.SecurityContextHolder.getContext().authentication
        val userDetails = authentication.principal as com.example.demo.security.service.UserDetailsImpl
        val user = userDetails.getUser()

        val userData: Map<String, Any> = mapOf(
            "id" to (user.id ?: 0L),
            "username" to user.username,
            "email" to user.email,
            "roles" to user.roles
        )

        return ApiResponse.success(userData)
    }
}
