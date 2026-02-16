package org.example.roulette.api.product.app

import org.example.roulette.api.product.domain.Product
import org.example.roulette.api.product.domain.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductCreateService(
    private val productRepository: ProductRepository,
    private val validator: ProductCreateRequestValidator,
) {
    fun createProduct(request: CreateProductRequest) {
        validator.validate(request)
        
        val product = Product(
            name = request.name,
            price = request.price,
        )
        productRepository.save(product)
    }
}