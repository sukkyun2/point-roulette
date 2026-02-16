package org.example.roulette.api.product.app

import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.product.domain.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductUpdateService(
    private val productRepository: ProductRepository,
    private val validator: ProductUpdateRequestValidator = ProductUpdateRequestValidator(),
) {
    fun updateProduct(
        id: Long,
        request: ProductUpdateRequest,
    ) {
        validator.validate(request)
        
        val product = productRepository.findByIdOrNull(id)
            ?: throw NoDataException("상품을 찾을 수 없습니다.")

        val updatedProduct = product.update(
            name = request.name,
            price = request.price,
        )
        
        productRepository.save(updatedProduct)
    }
}