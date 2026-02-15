package org.example.roulette.api.user.app

import io.swagger.v3.oas.annotations.media.Schema
import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.user.domain.UserQueryService
import org.example.roulette.config.auth.JwtUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userQueryService: UserQueryService,
    private val jwtUtil: JwtUtil,
) {
    fun login(nickname: String): LoginResult {
        val user = userQueryService.findByNickname(nickname) ?: throw NoDataException()
        val token = jwtUtil.generateToken(user.id, user.nickname)

        return LoginResult(
            token = token,
            userId = user.id,
            nickname = user.nickname,
            userBalance = user.balance,
        )
    }
}

@Schema(description = "로그인 응답 데이터")
data class LoginResult(
    @Schema(description = "JWT 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    val token: String,
    @Schema(description = "사용자 ID", example = "1")
    val userId: Long,
    @Schema(description = "사용자 닉네임", example = "홍길동")
    val nickname: String,
    @Schema(description = "사용자 보유 잔액", example = "5000")
    val userBalance: Long = 0,
)
