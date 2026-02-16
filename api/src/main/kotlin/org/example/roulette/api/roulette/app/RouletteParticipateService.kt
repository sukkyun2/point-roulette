package org.example.roulette.api.roulette.app

import org.example.roulette.api.point.app.PointBalanceService
import org.example.roulette.api.point.domain.Point
import org.example.roulette.api.roulette.domain.Roulette
import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
import org.example.roulette.api.roulette.domain.RouletteHistory
import org.example.roulette.api.roulette.domain.RouletteHistoryRepository
import org.example.roulette.api.roulette.domain.RouletteStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class RouletteParticipateService(
    private val rouletteBudgetRepository: RouletteBudgetRepository,
    private val rouletteHistoryRepository: RouletteHistoryRepository,
    private val pointBalanceService: PointBalanceService,
) {
    fun participate(userId: Long): Point {
        val today = LocalDate.now()

        if (existsRouletteHistory(userId, today)) {
            throw AlreadyParticipatedTodayException()
        }

        val rouletteBudget =
            rouletteBudgetRepository.findByBudgetDate(today)
                ?: throw EventPeriodException()

        val roulette = Roulette(rouletteBudget)
        if (!roulette.canParticipate()) {
            throw InsufficientBudgetException()
        }

        val earnPoints = roulette.participate()
        val rouletteHistory = RouletteHistory(userId = userId, earnPoint = earnPoints.value, eventDate = today)

        rouletteHistoryRepository.save(rouletteHistory)
        rouletteBudgetRepository.save(roulette.getRouletteBudget())
        pointBalanceService.addPoints(userId, earnPoints.value)

        return earnPoints
    }

    private fun existsRouletteHistory(
        userId: Long,
        eventDate: LocalDate,
    ): Boolean = rouletteHistoryRepository.existsByUserIdAndEventDateAndStatus(userId, eventDate, RouletteStatus.SUCCESS)
}
