package org.example.roulette.api.roulette.app

import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.roulette.domain.RouletteBudget
import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RouletteBudgetUpdateService(
    private val rouletteBudgetRepository: RouletteBudgetRepository,
    private val validator: RouletteBudgetUpdateValidator,
) {
    fun updateRouletteBudget(request: RouletteBudgetUpdateRequest) {
        validator.validate(request.budgetDate, request.totalBudget)

        val existingBudget = rouletteBudgetRepository.findByBudgetDate(request.budgetDate)
        requireNotNull(existingBudget) {
            throw NoDataException("해당 날짜의 예산이 존재하지 않습니다")
        }

        existingBudget.updateBudget(request.totalBudget)
        rouletteBudgetRepository.save(existingBudget)
    }
}
