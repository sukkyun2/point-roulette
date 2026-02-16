package org.example.roulette.api.point.app

import org.example.roulette.api.point.domain.PointHistory
import org.example.roulette.api.point.domain.PointHistoryRepository
import org.example.roulette.api.point.domain.PointType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PointHistoryAppender(
    private val pointHistoryRepository: PointHistoryRepository,
) {
    fun appendPointHistory(
        userId: Long,
        amount: Long,
        type: PointType,
    ): PointHistory {
        val pointHistory =
            PointHistory(
                userId = userId,
                amount = amount,
                type = type,
            )
        return pointHistoryRepository.save(pointHistory)
    }
}
