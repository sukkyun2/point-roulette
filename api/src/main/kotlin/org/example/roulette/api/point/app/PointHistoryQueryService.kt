package org.example.roulette.api.point.app

import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.point.domain.PointHistory
import org.example.roulette.api.point.domain.PointHistoryRepository
import org.example.roulette.api.point.domain.PointType
import org.example.roulette.api.user.domain.UserQueryService
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PointHistoryQueryService(
    private val pointHistoryRepository: PointHistoryRepository,
    private val userQueryService: UserQueryService,
) {
    fun getHistories(userId: Long): PointHistoryQueryResponse {
        val pointHistories = getPointHistory(userId)
        val availableBalance = calculateAvailableBalance(userId)
        val expiringSoonBalance = calculateExpiringSoonBalance(userId)

        val history =
            pointHistories.map {
                PointHistoryItemResponse(
                    id = it.id,
                    amount = it.amount,
                    type = it.type,
                    createdAt = it.createdAt,
                    expiresAt = it.getExpiresAt(),
                )
            }

        val balance =
            BalanceQueryResponse(
                available = availableBalance,
                expiringSoon = expiringSoonBalance,
            )

        return PointHistoryQueryResponse(balance, history)
    }

    fun getPointHistory(userId: Long): List<PointHistory> =
        pointHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId)

    fun calculateAvailableBalance(userId: Long): Long =
        userQueryService.getUser(userId).balance

    fun calculateExpiringSoonBalance(userId: Long): Long {
        val now = LocalDateTime.now()
        val thirtyDaysAgo = now.minusDays(30)

        val expiringSoonPoints =
            pointHistoryRepository.findExpiringSoonPointsByUserId(
                userId = userId,
                from = thirtyDaysAgo.minusDays(7),
                to = thirtyDaysAgo,
            )

        return expiringSoonPoints.filter { !it.isExpired() }.filter { it.type == PointType.EARN }.sumOf { it.amount }
    }
}
