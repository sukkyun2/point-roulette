package org.example.roulette.api.roulette.app

import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
import org.example.roulette.api.roulette.domain.RouletteHistoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class AdminDashboardQueryService(
    private val rouletteBudgetRepository: RouletteBudgetRepository,
    private val rouletteHistoryRepository: RouletteHistoryRepository,
) {
    fun getDashboard(targetDate: LocalDate): AdminDashboardQueryResponse {
        val rouletteBudget = rouletteBudgetRepository.findByBudgetDate(targetDate)
        val participantCount = rouletteHistoryRepository.countDistinctUsersByEventDate(targetDate)

        return AdminDashboardQueryResponse(
            totalBudget = rouletteBudget?.totalBudget ?: 0L,
            remainingBudget = rouletteBudget?.remainingBudget ?: 0L,
            participantCount = participantCount,
        )
    }
}
