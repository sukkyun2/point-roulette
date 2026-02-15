package org.example.roulette.api.product.app

import org.example.roulette.api.product.domain.Product

data class ProductListQueryResponse(
    val id: Long,
    val name: String,
    val price: Long,
) {
    companion object {
        fun from(product: Product): ProductListQueryResponse {
            return ProductListQueryResponse(
                id = product.id,
                name = product.name,
                price = product.price,
            )
        }
    }
}