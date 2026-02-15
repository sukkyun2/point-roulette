package org.example.roulette.api.roulette.api

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "룰렛 참여 응답")
data class RouletteParticipateResponse(
    @Schema(description = "획득한 포인트", example = "500")
    val value: Long,
)
