package org.example.roulette.api.roulette.app

import org.example.roulette.api.roulette.domain.RouletteBudget
import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@Service
@Transactional
class RouletteBudgetUpdateService(
    private val rouletteBudgetRepository: RouletteBudgetRepository,
) {
    fun updateRouletteBudget(request: RouletteBudgetUpdateRequest) {
        validateBudgetDate(request.budgetDate)
        validateBudgetAmount(request.totalBudget)

        val existingBudget = rouletteBudgetRepository.findByBudgetDate(request.budgetDate)
        requireNotNull(existingBudget) {
            "해당 날짜의 예산이 존재하지 않습니다"
        }

        updateExistingBudget(existingBudget, request.totalBudget)
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

    private fun updateExistingBudget(
        existingBudget: RouletteBudget,
        newTotalBudget: Long,
    ) {
        val usedBudget = existingBudget.totalBudget - existingBudget.remainingBudget
        val newRemainingBudget = newTotalBudget - usedBudget

        require(newRemainingBudget >= 0) {
            "현재 남아있는 예산(${existingBudget.remainingBudget}p)보다 낮게 수정할 수 없습니다"
        }

        val updatedBudget =
            existingBudget.copy(
                totalBudget = newTotalBudget,
                remainingBudget = newRemainingBudget,
                updatedAt = LocalDateTime.now(),
            )

        rouletteBudgetRepository.save(updatedBudget)
    }
}
