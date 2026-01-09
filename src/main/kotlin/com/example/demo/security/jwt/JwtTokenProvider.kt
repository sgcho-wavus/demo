package com.example.demo.security.jwt

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}")
    private val jwtSecret: String,

    @Value("\${jwt.access-token-expiration}")
    private val accessTokenExpiration: Long,

    @Value("\${jwt.refresh-token-expiration}")
    private val refreshTokenExpiration: Long
) {

    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    /**
     * Access Token 생성
     */
    fun generateAccessToken(authentication: Authentication): String {
        return generateToken(authentication.name, accessTokenExpiration)
    }

    /**
     * Refresh Token 생성
     */
    fun generateRefreshToken(authentication: Authentication): String {
        return generateToken(authentication.name, refreshTokenExpiration)
    }

    /**
     * 토큰 생성
     */
    private fun generateToken(username: String, expiration: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)

        return Jwts.builder()
            .subject(username)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key, Jwts.SIG.HS256)
            .compact()
    }

    /**
     * 토큰에서 사용자 이름 추출
     */
    fun getUsernameFromToken(token: String): String {
        val claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims.subject
    }

    /**
     * 토큰 유효성 검증
     */
    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            return true
        } catch (ex: SecurityException) {
            // 잘못된 JWT 서명
        } catch (ex: MalformedJwtException) {
            // 잘못된 JWT 토큰
        } catch (ex: ExpiredJwtException) {
            // 만료된 JWT 토큰
        } catch (ex: UnsupportedJwtException) {
            // 지원하지 않는 JWT 토큰
        } catch (ex: IllegalArgumentException) {
            // JWT 토큰이 비어있음
        }
        return false
    }
}
