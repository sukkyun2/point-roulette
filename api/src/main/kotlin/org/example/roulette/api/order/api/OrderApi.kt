package org.example.roulette.api.order.api

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.order.app.InsufficientPointException
import org.example.roulette.api.order.app.OrderService
import org.example.roulette.config.auth.CurrentUser
import org.example.roulette.config.auth.SimpleUser
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderApi(
    private val orderService: OrderService
) {
    @PostMapping("/api/orders")
    fun createOrder(
        @CurrentUser user: SimpleUser,
        @RequestBody request: OrderRequest
    ): ApiResponse<Nothing> {
        return try {
            orderService.createOrder(user.id, request.productId)
            ApiResponse.ok()
        } catch (ex: InsufficientPointException) {
            ApiResponse.badRequest(ex.message)
        }
    }
}

data class OrderRequest(
    val productId: Long
)