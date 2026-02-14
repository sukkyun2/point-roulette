package org.example.roulette.api.user.domain

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    val userId: Long = 0,

    @Column(name = "nickname")
    val nickname: String,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)