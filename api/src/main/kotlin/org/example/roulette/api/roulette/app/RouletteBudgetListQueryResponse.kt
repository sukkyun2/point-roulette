package org.example.roulette.api.roulette.app

import java.time.LocalDate
import java.time.LocalDateTime

data class RouletteBudgetListQueryResponse(
    val items: List<Item>,
) {
    data class Item(
        val budgetDate: LocalDate,
        val totalBudget: Long,
        val remainingBudget: Long,
        val updatedAt: LocalDateTime,
    )
}
