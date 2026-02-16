package org.example.roulette.api.roulette.app

import org.example.roulette.api.roulette.domain.DailyBudgetRepository
import org.example.roulette.api.roulette.domain.RouletteHistoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class AdminDashboardQueryService(
    private val dailyBudgetRepository: DailyBudgetRepository,
    private val rouletteHistoryRepository: RouletteHistoryRepository,
) {
    fun getDashboard(targetDate: LocalDate): AdminDashboardQueryResponse {
        val dailyBudget = dailyBudgetRepository.findByBudgetDate(targetDate)
        val participantCount = rouletteHistoryRepository.countDistinctUsersByEventDate(targetDate)

        return AdminDashboardQueryResponse(
            totalBudget = dailyBudget?.totalBudget ?: 0L,
            remainingBudget = dailyBudget?.remainingBudget ?: 0L,
            participantCount = participantCount,
        )
    }
}
