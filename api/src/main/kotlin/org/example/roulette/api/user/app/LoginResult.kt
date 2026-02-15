package org.example.roulette.api.user.app

import io.swagger.v3.oas.annotations.media.Schema

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
