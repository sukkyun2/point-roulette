package org.example.roulette.api.roulette.app

import org.example.roulette.api.common.app.ValidationException
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class RouletteBudgetCreateValidator {
    companion object {
        private const val INVALID_BUDGET_DATE_MESSAGE = "현재날짜보다 이전날짜의 예산을 조정할 수 없습니다"
        private const val INVALID_BUDGET_AMOUNT_MESSAGE = "예산은 0보다 커야 합니다"
    }

    fun validate(
        budgetDate: LocalDate,
        totalBudget: Long,
    ) {
        validateBudgetDate(budgetDate)
        validateBudgetAmount(totalBudget)
    }

    private fun validateBudgetDate(budgetDate: LocalDate) {
        if (budgetDate.isBefore(LocalDate.now())) {
            throw ValidationException(INVALID_BUDGET_DATE_MESSAGE)
        }
    }

    private fun validateBudgetAmount(totalBudget: Long) {
        if (totalBudget <= 0) {
            throw ValidationException(INVALID_BUDGET_AMOUNT_MESSAGE)
        }
    }
}

@Component
class RouletteBudgetUpdateValidator {
    companion object {
        private const val INVALID_BUDGET_DATE_MESSAGE = "현재날짜보다 이전날짜의 예산을 조정할 수 없습니다"
        private const val INVALID_BUDGET_AMOUNT_MESSAGE = "예산은 0보다 커야 합니다"
    }

    fun validate(
        budgetDate: LocalDate,
        totalBudget: Long,
    ) {
        validateBudgetDate(budgetDate)
        validateBudgetAmount(totalBudget)
    }

    private fun validateBudgetDate(budgetDate: LocalDate) {
        if (budgetDate.isBefore(LocalDate.now())) {
            throw ValidationException(INVALID_BUDGET_DATE_MESSAGE)
        }
    }

    private fun validateBudgetAmount(totalBudget: Long) {
        if (totalBudget <= 0) {
            throw ValidationException(INVALID_BUDGET_AMOUNT_MESSAGE)
        }
    }
}
