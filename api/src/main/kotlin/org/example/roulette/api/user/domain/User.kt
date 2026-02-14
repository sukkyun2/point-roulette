package org.example.roulette.api.user.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val userId: Long = 0,
    @Column(name = "nickname")
    val nickname: String,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
