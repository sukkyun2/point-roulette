package org.example.roulette.api.order.app

import org.example.roulette.api.order.domain.OrderStatus
import java.time.LocalDateTime

data class OrderHistoryListQueryResponse(
    val orders: List<OrderHistoryItemResponse>,
)

data class OrderHistoryItemResponse(
    val id: Long,
    val productName: String,
    val deductedPoints: Long,
    val status: OrderStatus,
    val createdAt: LocalDateTime,
)
