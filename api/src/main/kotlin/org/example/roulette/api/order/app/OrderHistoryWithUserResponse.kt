package org.example.roulette.api.order.app

import org.example.roulette.api.order.domain.OrderStatus
import java.time.LocalDateTime

data class OrderListQueryWithUserResponse(
    val orders: List<OrderItemResponse>,
)

data class OrderItemResponse(
    val id: Long,
    val nickName: String,
    val productName: String,
    val deductedPoints: Long,
    val status: OrderStatus,
    val createdAt: LocalDateTime,
)
