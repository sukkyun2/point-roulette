package org.example.roulette.api.roulette.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.roulette.api.point.domain.Point
import org.example.roulette.api.roulette.app.InsufficientBudgetException
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "roulette_budget")
data class RouletteBudget(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,
    @Column(name = "budget_date")
    val budgetDate: LocalDate,
    @Column(name = "total_budget")
    var totalBudget: Long,
    @Column(name = "remaining_budget")
    var remainingBudget: Long,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    fun deductBudget(point: Point) {
        val pointValue = point.value
        require(remainingBudget >= pointValue) { throw InsufficientBudgetException() }

        remainingBudget -= pointValue
        updatedAt = LocalDateTime.now()
    }

    fun canParticipate(minRequiredPoints: Long): Boolean = remainingBudget >= minRequiredPoints

    fun addBudget(point: Point) {
        remainingBudget += point.value
        updatedAt = LocalDateTime.now()
    }

    fun updateBudget(newTotalBudget: Long) {
        val usedBudget = totalBudget - remainingBudget
        val newRemainingBudget = newTotalBudget - usedBudget

        require(newRemainingBudget >= 0) {
            throw InsufficientBudgetException("사용한 예산(${usedBudget}p)보다 낮게 수정할 수 없습니다")
        }

        totalBudget = newTotalBudget
        remainingBudget = newRemainingBudget
        updatedAt = LocalDateTime.now()
    }
}
