package org.example.roulette.api.product.app

data class CreateProductRequest(
    val name: String,
    val price: Long,
)