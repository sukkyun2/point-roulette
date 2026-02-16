package org.example.roulette.api.roulette.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface RouletteBudgetRepository : JpaRepository<RouletteBudget, Long> {
    fun findByBudgetDate(budgetDate: LocalDate): RouletteBudget?
}
