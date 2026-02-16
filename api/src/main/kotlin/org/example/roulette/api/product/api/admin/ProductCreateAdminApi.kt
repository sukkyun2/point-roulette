package org.example.roulette.api.product.api.admin

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.common.app.ValidationException
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
    ): ApiResponse<Unit> =
        try {
            productCreateService.createProduct(request)
            ApiResponse.ok()
        } catch (ex: ValidationException) {
            ApiResponse.badRequest(ex.message)
        }
}
