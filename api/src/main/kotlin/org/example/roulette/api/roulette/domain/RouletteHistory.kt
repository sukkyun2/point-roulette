package org.example.roulette.api.roulette.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "roulette_history")
data class RouletteHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,
    @Column(name = "user_id")
    val userId: Long,
    @Column(name = "event_date")
    val eventDate: LocalDate,
    @Column(name = "earn_point")
    val earnPoint: Long,
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    var status: RouletteStatus = RouletteStatus.SUCCESS,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {
    fun isCanceled() = status == RouletteStatus.CANCELED

    fun cancel() {
        if (isCanceled()) {
            throw IllegalStateException("이미 취소된 룰렛 참여입니다.")
        }

        status = RouletteStatus.CANCELED
    }
}

enum class RouletteStatus {
    SUCCESS,
    CANCELED,
}
