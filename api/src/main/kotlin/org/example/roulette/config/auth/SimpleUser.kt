package org.example.roulette.config.auth

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 정보")
data class SimpleUser(
    @Schema(description = "사용자 ID", example = "1")
    val id: Long,
    
    @Schema(description = "사용자 닉네임", example = "홍길동")
    val nickname: String,
    
    @Schema(description = "사용자 보유 잔액", example = "5000")
    val userBalance: Long,
)
