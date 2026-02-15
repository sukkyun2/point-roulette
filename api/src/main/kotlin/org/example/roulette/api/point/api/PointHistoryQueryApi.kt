package org.example.roulette.api.point.api

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.point.domain.PointType
import org.example.roulette.config.auth.CurrentUser
import org.example.roulette.config.auth.SimpleUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class PointHistoryQueryApi {
    
    @GetMapping("/api/point/history")
    fun getPointHistory(@CurrentUser user: SimpleUser): ApiResponse<PointHistoryQueryResponse> {
        val now = LocalDateTime.now()
        val history = listOf(
            PointHistoryItemResponse(
                id = 1L,
                amount = 1000L,
                type = PointType.USE,
                createdAt = now.minusDays(10),
                expiresAt = now.plusDays(5),
            ),
            PointHistoryItemResponse(
                id = 2L,
                amount = -500L,
                type = PointType.USE,
                createdAt = now.minusDays(5),
                expiresAt = null,
            ),
            PointHistoryItemResponse(
                id = 3L,
                amount = 800L,
                type = PointType.EARN,
                createdAt = now.minusDays(20),
                expiresAt = now.minusDays(1),
            ),
            PointHistoryItemResponse(
                id = 4L,
                amount = 1500L,
                type = PointType.EARN,
                createdAt = now.minusDays(3),
                expiresAt = now.plusDays(3),
            )
        )
        
        val balance = BalanceQueryResponse(
            available = 2500L,
            expiringSoon = 2500L
        )
        
        return ApiResponse.ok(PointHistoryQueryResponse(balance, history))
    }
}

data class PointHistoryQueryResponse(
    val balance: BalanceQueryResponse,
    val history: List<PointHistoryItemResponse>
)

data class PointHistoryItemResponse(
    val id: Long,
    val amount: Long,
    val type: PointType,
    val createdAt: LocalDateTime,
    val expiresAt: LocalDateTime?
)

data class BalanceQueryResponse(
    val available: Long,
    val expiringSoon: Long
)