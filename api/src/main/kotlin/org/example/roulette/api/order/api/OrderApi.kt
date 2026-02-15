package org.example.roulette.api.order.api

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.config.auth.CurrentUser
import org.example.roulette.config.auth.SimpleUser
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class OrderApi {
    @PostMapping("/api/orders")
    fun createOrder(
        @CurrentUser user: SimpleUser,
        @RequestBody request: OrderRequest
    ): ApiResponse<Nothing> {
        return ApiResponse.ok()
    }
}

data class OrderRequest(
    val productId: String
)