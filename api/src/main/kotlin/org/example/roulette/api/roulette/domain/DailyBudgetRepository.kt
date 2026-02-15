package org.example.roulette.api.roulette.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface DailyBudgetRepository : JpaRepository<DailyBudget, Long> {
    fun findByBudgetDate(budgetDate: LocalDate): DailyBudget?
}
