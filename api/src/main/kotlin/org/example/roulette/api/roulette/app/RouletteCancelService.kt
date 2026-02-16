package org.example.roulette.api.roulette.app

import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.point.app.PointBalanceService
import org.example.roulette.api.point.domain.Point
import org.example.roulette.api.point.domain.ReferenceType
import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
import org.example.roulette.api.roulette.domain.RouletteHistoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RouletteCancelService(
    private val rouletteHistoryRepository: RouletteHistoryRepository,
    private val rouletteBudgetRepository: RouletteBudgetRepository,
    private val pointBalanceService: PointBalanceService,
) {
    fun cancelParticipation(rouletteHistoryId: Long) {
        val rouletteHistory =
            rouletteHistoryRepository
                .findById(rouletteHistoryId)
                .orElseThrow { NoDataException() }

        rouletteHistory.cancel()

        val earnedPoint = Point(rouletteHistory.earnPoint)

        val rouletteBudget =
            rouletteBudgetRepository.findByBudgetDate(rouletteHistory.eventDate)
                ?: throw NoDataException()
        rouletteBudget.addBudget(earnedPoint)

        rouletteBudgetRepository.save(rouletteBudget)
        rouletteHistoryRepository.save(rouletteHistory)

        pointBalanceService.deductPoints(
            userId = rouletteHistory.userId,
            amount = earnedPoint.value,
            referenceType = ReferenceType.ROULETTE_CANCEL,
            referenceId = rouletteHistoryId,
        )
    }
}
