package org.example.roulette.api.roulette.app

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "룰렛 상태 조회 응답")
data class RouletteStatusQueryResponse(
    @Schema(description = "오늘 참여 여부", example = "false")
    val hasParticipatedToday: Boolean,
    @Schema(description = "남은 예산", example = "50000")
    val remainingBudget: Long,
)
