package org.example.roulette.api.order.api

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.order.app.OrderHistoryListQueryResponse
import org.example.roulette.api.order.app.OrderHistoryQueryService
import org.example.roulette.api.order.domain.OrderStatus
import org.example.roulette.config.auth.CurrentUser
import org.example.roulette.config.auth.SimpleUser
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class OrderHistoryQueryApi(
    private val orderHistoryQueryService: OrderHistoryQueryService,
) {
    @GetMapping("/api/orders/history")
    fun getOrderHistory(
        @CurrentUser user: SimpleUser,
    ): ApiResponse<OrderHistoryListQueryResponse> {
        val orders = orderHistoryQueryService.getOrderHistory(user.id)
        return ApiResponse.ok(OrderHistoryListQueryResponse(orders))
    }
}
