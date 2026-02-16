package org.example.roulette.api.roulette.app

import java.time.LocalDate

data class RouletteBudgetCreateRequest(
    val budgetDate: LocalDate,
    val totalBudget: Long,
)
