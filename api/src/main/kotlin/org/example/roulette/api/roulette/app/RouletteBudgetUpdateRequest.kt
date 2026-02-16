package org.example.roulette.api.roulette.app

import java.time.LocalDate

data class RouletteBudgetUpdateRequest(
    val budgetDate: LocalDate,
    val totalBudget: Long,
)
