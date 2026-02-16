package org.example.roulette.api.roulette.app

import org.example.roulette.api.roulette.domain.RouletteStatus
import java.time.LocalDateTime

data class RouletteParticipationHistoryListQueryResponse(
    val participations: List<RouletteParticipationHistoryItem>,
)

data class RouletteParticipationHistoryItem(
    val id: Long,
    val nickName: String,
    val earnPoint: Long,
    val status: RouletteStatus,
    val createdAt: LocalDateTime,
)
