package org.example.roulette.api.order.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderRepository : JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    fun findByUserIdOrderByCreatedAtDesc(userId: Long): List<Order>
}
