package org.example.roulette.api.order.api.admin

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.order.app.OrderCancelService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderCancelAdminApi(
    private val orderCancelService: OrderCancelService,
) {
    @PostMapping("/api/admin/orders/{orderId}/cancel")
    fun cancelOrder(
        @PathVariable orderId: Long,
    ): ApiResponse<Nothing> =
        try {
            orderCancelService.cancelOrder(orderId)
            ApiResponse.ok()
        } catch (ex: IllegalStateException) {
            ApiResponse.badRequest(ex.message)
        } catch (ex: IllegalArgumentException) {
            ApiResponse.badRequest(ex.message)
        }
}
