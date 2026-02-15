package org.example.roulette.api.user.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.roulette.api.point.domain.Point
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,
    @Column(name = "nickname")
    val nickname: String,
    @Column(name = "balance")
    var balance: Long = 0,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at")
    var updatedAt: LocalDateTime = LocalDateTime.now(),
) {
    fun addBalance(point: Point) {
        this.balance += point.value
        this.updatedAt = LocalDateTime.now()
    }

    fun deductBalance(point: Point) {
        require(this.balance >= point.value) { "잔액이 부족합니다" }

        this.balance -= point.value
        this.updatedAt = LocalDateTime.now()
    }
}
