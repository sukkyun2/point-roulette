package org.example.roulette.config.auth

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtUtil(
    @Value("\${jwt.secret}")
    private val secret: String,
    @Value("\${jwt.expiration}")
    private val expiration: Long,
) {
    private val key: SecretKey = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(
        userId: Long,
        nickname: String,
    ): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)

        return Jwts
            .builder()
            .subject(userId.toString())
            .claim("nickname", nickname)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact()
    }

    fun validateToken(token: String): Boolean =
        try {
            Jwts
                .parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: Exception) {
            false
        }

    fun getUserIdFromToken(token: String): Long {
        val claims = getClaims(token)
        return claims.subject.toLong()
    }

    fun getNicknameFromToken(token: String): String {
        val claims = getClaims(token)
        return claims.get("nickname", String::class.java)
    }

    private fun getClaims(token: String): Claims =
        Jwts
            .parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
}
