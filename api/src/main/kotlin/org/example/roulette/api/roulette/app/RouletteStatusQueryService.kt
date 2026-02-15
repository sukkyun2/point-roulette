package org.example.roulette.api.roulette.app

import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.roulette.api.RouletteStatusQueryResponse
import org.example.roulette.api.roulette.domain.DailyBudgetRepository
import org.example.roulette.api.roulette.domain.RouletteHistoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class RouletteStatusQueryService(
    private val dailyBudgetRepository: DailyBudgetRepository,
    private val rouletteHistoryRepository: RouletteHistoryRepository,
) {
    fun getStatus(userId: Long): RouletteStatusQueryResponse {
        val today = LocalDate.now()
        
        val hasParticipatedToday = rouletteHistoryRepository.existsByUserIdAndEventDate(userId, today)
        
        val dailyBudget = dailyBudgetRepository.findByBudgetDate(today)
            ?: throw NoDataException()
        
        return RouletteStatusQueryResponse(
            hasParticipatedToday = hasParticipatedToday,
            remainingBudget = dailyBudget.remainingBudget
        )
    }
}