package org.example.roulette.api.user.app

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
        )
    }
}

data class LoginResult(
    val token: String,
    val userId: Long,
    val nickname: String,
)
