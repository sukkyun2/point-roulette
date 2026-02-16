package org.example.roulette.api.order.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderRepository : JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    fun findByUserIdOrderByCreatedAtDesc(userId: Long): List<Order>

    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    fun findAllOrderByCreatedAtDesc(): List<Order>

    @Query("SELECT o FROM Order o WHERE o.status = :status ORDER BY o.createdAt DESC")
    fun findByStatusOrderByCreatedAtDesc(status: OrderStatus): List<Order>
}
