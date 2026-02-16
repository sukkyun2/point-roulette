package org.example.roulette.api.roulette.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface RouletteHistoryRepository : JpaRepository<RouletteHistory, Long> {
    fun existsByUserIdAndEventDateAndStatus(
        userId: Long,
        eventDate: LocalDate,
        status: RouletteStatus?,
    ): Boolean

    @Query("SELECT COUNT(DISTINCT rh.userId) FROM RouletteHistory rh WHERE rh.eventDate = :eventDate")
    fun countDistinctUsersByEventDate(
        @Param("eventDate") eventDate: LocalDate,
    ): Long

    @Query(
        "SELECT rh FROM RouletteHistory rh " +
            "WHERE (:status IS NULL OR rh.status = :status) " +
            "ORDER BY rh.createdAt DESC",
    )
    fun findAllByStatus(
        @Param("status") status: RouletteStatus?,
    ): List<RouletteHistory>

    fun findByEventDateAndStatus(
        eventDate: LocalDate,
        status: RouletteStatus,
    ): List<RouletteHistory>
}
