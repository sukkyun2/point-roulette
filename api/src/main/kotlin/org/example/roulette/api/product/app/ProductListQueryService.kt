package org.example.roulette.api.product.app

import org.example.roulette.api.product.app.ProductListQueryResponse
import org.example.roulette.api.product.domain.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductListQueryService(
    private val productRepository: ProductRepository,
) {
    fun findAll(): List<ProductListQueryResponse> {
        return productRepository.findAll()
            .map { ProductListQueryResponse.Companion.from(it) }
    }
}