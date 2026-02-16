package org.example.roulette.api.order.api.admin

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.order.app.OrderHistoryQueryService
import org.example.roulette.api.order.app.OrderListQueryWithUserResponse
import org.example.roulette.api.order.domain.OrderStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderListQueryAdminApi(
    private val orderHistoryQueryService: OrderHistoryQueryService,
) {
    @GetMapping("/api/admin/orders")
    fun getOrderList(
        @RequestParam(required = false) status: OrderStatus?,
    ): ApiResponse<OrderListQueryWithUserResponse> {
        val response = orderHistoryQueryService.getOrderHistories(status)
        return ApiResponse.ok(response)
    }
}
