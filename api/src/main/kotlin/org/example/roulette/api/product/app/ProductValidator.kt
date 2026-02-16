package org.example.roulette.api.product.app

import org.springframework.stereotype.Component

@Component
class ProductCreateRequestValidator {
    fun validate(request: CreateProductRequest) {
        require(request.name.isNotBlank()) { "상품명은 필수입니다." }
        require(request.price > 0) { "상품 가격은 0보다 커야 합니다." }
    }
}

@Component
class ProductUpdateRequestValidator {
    fun validate(request: ProductUpdateRequest) {
        require(request.name.isNotBlank()) { "상품명은 필수입니다." }
        require(request.price > 0) { "상품 가격은 0보다 커야 합니다." }
    }
}
