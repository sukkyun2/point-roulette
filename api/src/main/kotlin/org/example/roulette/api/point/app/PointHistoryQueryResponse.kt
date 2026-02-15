package org.example.roulette.api.point.app

import org.example.roulette.api.point.domain.PointType
import java.time.LocalDateTime

data class PointHistoryQueryResponse(
    val balance: BalanceQueryResponse,
    val history: List<PointHistoryItemResponse>,
)

data class PointHistoryItemResponse(
    val id: Long,
    val amount: Long,
    val type: PointType,
    val createdAt: LocalDateTime,
    val expiresAt: LocalDateTime?,
)

data class BalanceQueryResponse(
    val available: Long,
    val expiringSoon: Long,
)
