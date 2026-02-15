package org.example.roulette.api.point.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "point_history")
data class PointHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,
    @Column(name = "user_id")
    val userId: Long,
    @Column(name = "amount")
    val amount: Long,
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    val type: PointType,
    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type")
    val referenceType: ReferenceType,
    @Column(name = "reference_id")
    val referenceId: Long?,
    @Column(name = "expired_at")
    val expiresAt: LocalDateTime?,
    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),
)

enum class PointType {
    EARN,
    USE,
    REFUND,
    RECLAIM,
    EXPIRE,
}

enum class ReferenceType {
    ROULETTE,
    ORDER,
    NONE,
}
