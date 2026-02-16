package org.example.roulette.api.product.app

import org.example.roulette.api.common.app.ValidationException
import org.springframework.stereotype.Component

object ProductValidationMessages {
    const val PRODUCT_NAME_REQUIRED = "상품명은 필수입니다."
    const val PRODUCT_PRICE_MUST_BE_POSITIVE = "상품 가격은 0보다 커야 합니다."
}

@Component
class ProductCreateRequestValidator {
    fun validate(request: CreateProductRequest) {
        if (request.name.isBlank()) {
            throw ValidationException(ProductValidationMessages.PRODUCT_NAME_REQUIRED)
        }
        if (request.price <= 0) {
            throw ValidationException(ProductValidationMessages.PRODUCT_PRICE_MUST_BE_POSITIVE)
        }
    }
}

@Component
class ProductUpdateRequestValidator {
    fun validate(request: ProductUpdateRequest) {
        if (request.name.isBlank()) {
            throw ValidationException(ProductValidationMessages.PRODUCT_NAME_REQUIRED)
        }
        if (request.price <= 0) {
            throw ValidationException(ProductValidationMessages.PRODUCT_PRICE_MUST_BE_POSITIVE)
        }
    }
}
