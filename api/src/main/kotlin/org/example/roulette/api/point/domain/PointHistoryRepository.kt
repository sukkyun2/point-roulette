package org.example.roulette.api.point.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PointHistoryRepository : JpaRepository<PointHistory, Long> {
    @Query(
        """
        SELECT ph FROM PointHistory ph 
        WHERE ph.type = 'EARN' 
        AND ph.expiresAt <= :now 
        AND ph.userId NOT IN (
            SELECT DISTINCT ph2.userId FROM PointHistory ph2 
            WHERE ph2.type = 'EXPIRE' 
            AND ph2.referenceType = 'NONE' 
            AND ph2.referenceId = ph.id
        )
        """,
    )
    fun findExpiredPointHistories(now: LocalDateTime): List<PointHistory>
}
