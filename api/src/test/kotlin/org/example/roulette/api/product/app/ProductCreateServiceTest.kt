package org.example.roulette.api.product.app

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.example.roulette.api.common.app.ValidationException
import org.example.roulette.api.product.domain.Product
import org.example.roulette.api.product.domain.ProductRepository

class ProductCreateServiceTest :
    FunSpec({
        val productRepository = mockk<ProductRepository>()
        val validator = mockk<ProductCreateRequestValidator>()
        lateinit var productCreateService: ProductCreateService

        beforeTest {
            clearMocks(productRepository, validator)
            productCreateService =
                ProductCreateService(
                    productRepository = productRepository,
                    validator = validator,
                )
        }

        context("상품 생성 관련 테스트") {
            test("정상적인 상품 생성 시 성공적으로 저장되어야 한다") {
                // given
                val request = createProductRequest(name = "테스트 상품", price = 1000L)
                val expectedProduct = createProduct(name = "테스트 상품", price = 1000L)
                val slot = slot<Product>()

                justRun { validator.validate(request) }
                every { productRepository.save(capture(slot)) } returns expectedProduct

                // when
                productCreateService.createProduct(request)

                // then
                val capturedProduct = slot.captured
                capturedProduct.name shouldBe request.name
                capturedProduct.price shouldBe request.price

                verify(exactly = 1) { validator.validate(request) }
                verify(exactly = 1) { productRepository.save(any()) }
            }

            test("상품명이 빈 값인 경우 ValidationException이 발생해야 한다") {
                // given
                val request = createProductRequest(name = "", price = 1000L)

                every { validator.validate(request) } throws ValidationException(ProductValidationMessages.PRODUCT_NAME_REQUIRED)

                // when & then
                shouldThrow<ValidationException> {
                    productCreateService.createProduct(request)
                }

                verify(exactly = 1) { validator.validate(request) }
                verify(exactly = 0) { productRepository.save(any()) }
            }

            test("상품 가격이 0 이하인 경우 ValidationException이 발생해야 한다") {
                // given
                val request = createProductRequest(name = "테스트 상품", price = 0L)

                every { validator.validate(request) } throws ValidationException(ProductValidationMessages.PRODUCT_PRICE_MUST_BE_POSITIVE)

                // when & then
                shouldThrow<ValidationException> {
                    productCreateService.createProduct(request)
                }

                verify(exactly = 1) { validator.validate(request) }
                verify(exactly = 0) { productRepository.save(any()) }
            }

            test("상품 가격이 음수인 경우 ValidationException이 발생해야 한다") {
                // given
                val request = createProductRequest(name = "테스트 상품", price = -1000L)

                every { validator.validate(request) } throws ValidationException(ProductValidationMessages.PRODUCT_PRICE_MUST_BE_POSITIVE)

                // when & then
                shouldThrow<ValidationException> {
                    productCreateService.createProduct(request)
                }

                verify(exactly = 1) { validator.validate(request) }
                verify(exactly = 0) { productRepository.save(any()) }
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

        private fun createProduct(
            id: Long = 1L,
            name: String = "테스트 상품",
            price: Long = 1000L,
        ): Product =
            mockk {
                every { this@mockk.id } returns id
                every { this@mockk.name } returns name
                every { this@mockk.price } returns price
            }
    }
}
