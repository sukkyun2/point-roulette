package org.example.roulette.api.roulette.app

import org.example.roulette.api.common.app.ValidationException
import org.example.roulette.api.roulette.domain.RouletteBudget
import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RouletteBudgetCreateService(
    private val rouletteBudgetRepository: RouletteBudgetRepository,
    private val rouletteBudgetCreateValidator: RouletteBudgetCreateValidator,
) {
    fun createRouletteBudget(request: RouletteBudgetCreateRequest) {
        rouletteBudgetCreateValidator.validate(request.budgetDate, request.totalBudget)

        val existingBudget = rouletteBudgetRepository.findByBudgetDate(request.budgetDate)
        require(existingBudget == null) {
            throw ValidationException("해당 날짜의 예산이 이미 존재합니다")
        }

        val newBudget =
            RouletteBudget(
                budgetDate = request.budgetDate,
                totalBudget = request.totalBudget,
                remainingBudget = request.totalBudget,
            )

        rouletteBudgetRepository.save(newBudget)
    }
}
