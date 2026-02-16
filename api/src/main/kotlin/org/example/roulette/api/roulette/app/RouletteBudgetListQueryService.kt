package org.example.roulette.api.roulette.app

import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RouletteBudgetListQueryService(
    private val rouletteBudgetRepository: RouletteBudgetRepository,
) {
    fun getRouletteBudgetList(): RouletteBudgetListQueryResponse {
        val budgets = rouletteBudgetRepository.findAllByOrderByBudgetDateDesc()

        val items =
            budgets.map { budget ->
                RouletteBudgetListQueryResponse.Item(
                    budgetDate = budget.budgetDate,
                    totalBudget = budget.totalBudget,
                    remainingBudget = budget.remainingBudget,
                    updatedAt = budget.updatedAt,
                )
            }

        return RouletteBudgetListQueryResponse(items)
    }
}
