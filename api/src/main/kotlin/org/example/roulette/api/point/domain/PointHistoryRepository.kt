package org.example.roulette.api.point.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PointHistoryRepository : JpaRepository<PointHistory, Long> {
    fun findByUserIdOrderByCreatedAtDesc(userId: Long): List<PointHistory>

    @Query(
        """
        SELECT ph FROM PointHistory ph 
        WHERE ph.userId = :userId 
        AND ph.type = 'EARN' 
        AND ph.createdAt > :expirationThreshold
        """,
    )
    fun findActiveEarnPointsByUserId(
        userId: Long,
        expirationThreshold: LocalDateTime,
    ): List<PointHistory>

    @Query(
        """
        SELECT ph FROM PointHistory ph 
        WHERE ph.userId = :userId 
        AND ph.type = 'EARN' 
        AND ph.createdAt > :from 
        AND ph.createdAt <= :to
        """,
    )
    fun findExpiringSoonPointsByUserId(
        userId: Long,
        from: LocalDateTime,
        to: LocalDateTime,
    ): List<PointHistory>
}
