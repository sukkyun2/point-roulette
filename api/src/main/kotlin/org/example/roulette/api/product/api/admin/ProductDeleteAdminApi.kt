package org.example.roulette.api.product.api.admin

import org.example.roulette.api.common.api.ApiResponse
import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.product.app.ProductDeleteService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductDeleteAdminApi(
    private val productDeleteService: ProductDeleteService,
) {
    @DeleteMapping("/api/admin/products/{id}")
    fun deleteProduct(
        @PathVariable id: Long,
    ): ApiResponse<Unit> =
        try {
            productDeleteService.deleteProduct(id)
            ApiResponse.ok()
        } catch (ex: NoDataException) {
            ApiResponse.nodata()
        }
}
