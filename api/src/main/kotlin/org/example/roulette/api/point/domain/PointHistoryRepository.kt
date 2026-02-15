package org.example.roulette.api.point.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface PointHistoryRepository : JpaRepository<PointHistory, Long>
