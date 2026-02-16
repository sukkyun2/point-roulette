package org.example.roulette.api.product.api

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.common.api.SwaggerApiResponse
import org.example.roulette.api.product.app.CreateProductRequest
import org.example.roulette.api.product.app.ProductCreateService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductCreateAdminApi(
    private val productCreateService: ProductCreateService,
) {
    @PostMapping("/api/admin/products")
    fun createProduct(
        @RequestBody request: CreateProductRequest,
    ): ApiResponse<Unit> {
        productCreateService.createProduct(request)
        return ApiResponse.ok()
    }
}