package org.example.roulette.api.roulette.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface RouletteHistoryRepository : JpaRepository<RouletteHistory, Long> {
    fun findByUserIdAndEventDate(
        userId: Long,
        eventDate: LocalDate,
    ): RouletteHistory?

    fun existsByUserIdAndEventDate(
        userId: Long,
        eventDate: LocalDate,
    ): Boolean
}
