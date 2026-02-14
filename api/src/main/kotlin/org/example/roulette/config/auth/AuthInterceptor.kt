package org.example.roulette.config.auth

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.example.roulette.api.common.api.ApiResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class AuthInterceptor(
    private val jwtUtil: JwtUtil,
    private val objectMapper: ObjectMapper,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorized(response)
            return false
        }

        val token = authHeader.substring(7)

        if (!jwtUtil.validateToken(token)) {
            sendUnauthorized(response)
            return false
        }

        try {
            val userId = jwtUtil.getUserIdFromToken(token)
            val nickname = jwtUtil.getNicknameFromToken(token)

            request.setAttribute("userId", userId)
            request.setAttribute("nickname", nickname)

            return true
        } catch (e: Exception) {
            sendUnauthorized(response)
            return false
        }
    }

    private fun sendUnauthorized(response: HttpServletResponse) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        val errorResponse = ApiResponse.unauthorized()
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}
