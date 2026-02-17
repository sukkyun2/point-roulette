package org.example.roulette.api.product.app

import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.product.domain.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductDeleteService(
    private val productRepository: ProductRepository,
) {
    fun deleteProduct(id: Long) {
        val product = productRepository.findByIdNotDeleted(id) ?: throw NoDataException("Product not found with id: $id")
        val deletedProduct = product.softDelete()
        productRepository.save(deletedProduct)
    }
}
