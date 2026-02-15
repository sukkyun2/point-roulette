package org.example.roulette.api.product.api

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.common.api.SwaggerApiResponse
import org.example.roulette.api.product.app.ProductListQueryResponse
import org.example.roulette.api.product.app.ProductListQueryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductListQueryApi(
    private val productListQueryService: ProductListQueryService,
) {
    @GetMapping("/api/products")
    @SwaggerApiResponse(schema = ProductListQueryResponse::class)
    fun getProducts(): ApiResponse<List<ProductListQueryResponse>> {
        val products = productListQueryService.findAll()
        return ApiResponse.ok(products)
    }
}