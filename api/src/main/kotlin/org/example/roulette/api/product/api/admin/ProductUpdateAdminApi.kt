package org.example.roulette.api.product.api.admin

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.common.app.ValidationException
import org.example.roulette.api.product.app.ProductUpdateRequest
import org.example.roulette.api.product.app.ProductUpdateService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductUpdateAdminApi(
    private val productUpdateService: ProductUpdateService,
) {
    @PutMapping("/api/admin/products/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody request: ProductUpdateRequest,
    ): ApiResponse<Unit> =
        try {
            productUpdateService.updateProduct(id, request)
            ApiResponse.ok()
        } catch (ex: ValidationException) {
            ApiResponse.badRequest(ex.message)
        }
}
