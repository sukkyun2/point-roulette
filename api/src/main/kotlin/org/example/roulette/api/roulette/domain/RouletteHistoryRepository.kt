package org.example.roulette.api.roulette.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface RouletteHistoryRepository : JpaRepository<RouletteHistory, Long> {
    fun existsByUserIdAndEventDate(
        userId: Long,
        eventDate: LocalDate,
    ): Boolean
}
