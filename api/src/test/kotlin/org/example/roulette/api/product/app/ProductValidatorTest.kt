package org.example.roulette.api.product.app

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.roulette.api.common.app.ValidationException

class ProductValidatorTest :
    FunSpec({

        context("ProductCreateRequestValidator 테스트") {
            val validator = ProductCreateRequestValidator()

            test("정상적인 상품 생성 요청은 검증을 통과해야 한다") {
                // given
                val request = createProductRequest(name = "정상 상품", price = 1000L)

                // when & then
                validator.validate(request)
            }

            test("상품명이 빈 문자열인 경우 ValidationException이 발생해야 한다") {
                // given
                val request = createProductRequest(name = "", price = 1000L)

                // when & then
                val exception =
                    shouldThrow<ValidationException> {
                        validator.validate(request)
                    }
                exception.message shouldBe ProductValidationMessages.PRODUCT_NAME_REQUIRED
            }

            test("상품 가격이 0인 경우 ValidationException이 발생해야 한다") {
                // given
                val request = createProductRequest(name = "테스트 상품", price = 0L)

                // when & then
                val exception =
                    shouldThrow<ValidationException> {
                        validator.validate(request)
                    }
                exception.message shouldBe ProductValidationMessages.PRODUCT_PRICE_MUST_BE_POSITIVE
            }

            test("상품 가격이 음수인 경우 ValidationException이 발생해야 한다") {
                // given
                val request = createProductRequest(name = "테스트 상품", price = -1000L)

                // when & then
                val exception =
                    shouldThrow<ValidationException> {
                        validator.validate(request)
                    }
                exception.message shouldBe ProductValidationMessages.PRODUCT_PRICE_MUST_BE_POSITIVE
            }

            test("상품명과 가격이 모두 잘못된 경우 첫 번째 검증 실패에 대한 예외가 발생해야 한다") {
                // given
                val request = createProductRequest(name = "", price = -1000L)

                // when & then
                val exception =
                    shouldThrow<ValidationException> {
                        validator.validate(request)
                    }
                exception.message shouldBe ProductValidationMessages.PRODUCT_NAME_REQUIRED
            }
        }

        context("ProductUpdateRequestValidator 테스트") {
            val validator = ProductUpdateRequestValidator()

            test("정상적인 상품 업데이트 요청은 검증을 통과해야 한다") {
                // given
                val request = createProductUpdateRequest(name = "수정된 상품", price = 2000L)

                // when & then
                validator.validate(request)
            }

            test("상품명이 빈 문자열인 경우 ValidationException이 발생해야 한다") {
                // given
                val request = createProductUpdateRequest(name = "", price = 2000L)

                // when & then
                val exception =
                    shouldThrow<ValidationException> {
                        validator.validate(request)
                    }
                exception.message shouldBe ProductValidationMessages.PRODUCT_NAME_REQUIRED
            }

            test("상품명이 공백으로만 이루어진 경우 ValidationException이 발생해야 한다") {
                // given
                val request = createProductUpdateRequest(name = "   ", price = 2000L)

                // when & then
                val exception =
                    shouldThrow<ValidationException> {
                        validator.validate(request)
                    }
                exception.message shouldBe ProductValidationMessages.PRODUCT_NAME_REQUIRED
            }

            test("상품 가격이 0인 경우 ValidationException이 발생해야 한다") {
                // given
                val request = createProductUpdateRequest(name = "수정된 상품", price = 0L)

                // when & then
                val exception =
                    shouldThrow<ValidationException> {
                        validator.validate(request)
                    }
                exception.message shouldBe ProductValidationMessages.PRODUCT_PRICE_MUST_BE_POSITIVE
            }

            test("상품 가격이 음수인 경우 ValidationException이 발생해야 한다") {
                // given
                val request = createProductUpdateRequest(name = "수정된 상품", price = -2000L)

                // when & then
                val exception =
                    shouldThrow<ValidationException> {
                        validator.validate(request)
                    }
                exception.message shouldBe ProductValidationMessages.PRODUCT_PRICE_MUST_BE_POSITIVE
            }
        }
    }) {
    companion object {
        private fun createProductRequest(
            name: String = "테스트 상품",
            price: Long = 1000L,
        ): CreateProductRequest =
            CreateProductRequest(
                name = name,
                price = price,
            )

        private fun createProductUpdateRequest(
            name: String = "수정된 상품",
            price: Long = 2000L,
        ): ProductUpdateRequest =
            ProductUpdateRequest(
                name = name,
                price = price,
            )
    }
}
