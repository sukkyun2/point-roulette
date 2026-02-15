package org.example.roulette.api.user.app

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "로그인 요청 데이터")
data class LoginRequest(
    @Schema(description = "사용자 닉네임", example = "홍길동")
    val nickname: String,
)
