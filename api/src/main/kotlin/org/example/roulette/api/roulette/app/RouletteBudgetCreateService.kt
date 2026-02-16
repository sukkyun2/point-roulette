package org.example.roulette.api.roulette.app

import org.example.roulette.api.roulette.domain.RouletteBudget
import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
@Transactional
class RouletteBudgetCreateService(
    private val rouletteBudgetRepository: RouletteBudgetRepository,
) {
    fun createRouletteBudget(request: RouletteBudgetCreateRequest) {
        validateBudgetDate(request.budgetDate)
        validateBudgetAmount(request.totalBudget)

        val existingBudget = rouletteBudgetRepository.findByBudgetDate(request.budgetDate)
        require(existingBudget == null) {
            "해당 날짜의 예산이 이미 존재합니다"
        }

        createNewBudget(request)
    }

    private fun validateBudgetDate(budgetDate: LocalDate) {
        require(!budgetDate.isBefore(LocalDate.now())) {
            "현재날짜보다 이전날짜의 예산을 조정할 수 없습니다"
        }
    }

    private fun validateBudgetAmount(totalBudget: Long) {
        require(totalBudget > 0) {
            "예산은 0보다 커야 합니다"
        }
    }

    private fun createNewBudget(request: RouletteBudgetCreateRequest) {
        val newBudget =
            RouletteBudget(
                budgetDate = request.budgetDate,
                totalBudget = request.totalBudget,
                remainingBudget = request.totalBudget,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )

        rouletteBudgetRepository.save(newBudget)
    }
}
