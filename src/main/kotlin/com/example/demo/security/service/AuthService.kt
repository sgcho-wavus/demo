package com.example.demo.security.service

import com.example.demo.api.ErrorCode
import com.example.demo.exception.DemoBizException
import com.example.demo.security.domain.User
import com.example.demo.security.dto.JwtResponse
import com.example.demo.security.dto.LoginRequest
import com.example.demo.security.dto.SignUpRequest
import com.example.demo.security.jwt.JwtTokenProvider
import com.example.demo.security.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun signUp(request: SignUpRequest): JwtResponse {
        if (userRepository.existsByUsername(request.username)) {
            throw DemoBizException(ErrorCode.DUPLICATE_USERNAME.toErrorMessage(request.username))
        }

        if (userRepository.existsByEmail(request.email)) {
            throw DemoBizException(ErrorCode.DUPLICATE_EMAIL.toErrorMessage(request.email))
        }

        val user = User(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password)
        )

        val savedUser = userRepository.save(user)

        val authentication = UsernamePasswordAuthenticationToken(
            savedUser.username,
            request.password
        )

        val authenticatedAuth = authenticationManager.authenticate(authentication)
        SecurityContextHolder.getContext().authentication = authenticatedAuth

        val accessToken = jwtTokenProvider.generateAccessToken(authenticatedAuth)
        val refreshToken = jwtTokenProvider.generateRefreshToken(authenticatedAuth)

        return JwtResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            username = savedUser.username,
            email = savedUser.email,
            roles = savedUser.roles
        )
    }

    fun login(request: LoginRequest): JwtResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.username,
                request.password
            )
        )

        SecurityContextHolder.getContext().authentication = authentication

        val userDetails = authentication.principal as UserDetailsImpl
        val user = userDetails.getUser()

        val accessToken = jwtTokenProvider.generateAccessToken(authentication)
        val refreshToken = jwtTokenProvider.generateRefreshToken(authentication)

        return JwtResponse(
            accessToken = accessToken,
            refreshToken = refreshToken,
            username = user.username,
            email = user.email,
            roles = user.roles
        )
    }

    fun refreshAccessToken(refreshToken: String): JwtResponse {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw DemoBizException(ErrorCode.INVALID_TOKEN.toErrorMessage())
        }

        val username = jwtTokenProvider.getUsernameFromToken(refreshToken)
        val user = userRepository.findByUsername(username)
            .orElseThrow { DemoBizException(ErrorCode.USER_NOT_FOUND.toErrorMessage(username)) }

        val authentication = UsernamePasswordAuthenticationToken(
            username,
            null,
            UserDetailsImpl(user).authorities
        )

        val newAccessToken = jwtTokenProvider.generateAccessToken(authentication)
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication)

        return JwtResponse(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            username = user.username,
            email = user.email,
            roles = user.roles
        )
    }
}
