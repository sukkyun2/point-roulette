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
import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.common.app.ValidationException
import org.example.roulette.api.product.domain.Product
import org.example.roulette.api.product.domain.ProductRepository
import java.time.LocalDateTime

class ProductUpdateServiceTest :
    FunSpec({
        val productRepository = mockk<ProductRepository>()
        val validator = mockk<ProductUpdateRequestValidator>()
        lateinit var productUpdateService: ProductUpdateService

        beforeTest {
            clearMocks(productRepository, validator)
            productUpdateService =
                ProductUpdateService(
                    productRepository = productRepository,
                    validator = validator,
                )
        }

        context("상품 업데이트 관련 테스트") {
            test("정상적인 상품 업데이트 시 성공적으로 수정되어야 한다") {
                // given
                val productId = 1L
                val request = createProductUpdateRequest(name = "수정된 상품", price = 2000L)
                val existingProduct = createProduct(id = productId, name = "원래 상품", price = 1000L)
                val updatedProduct = createProduct(id = productId, name = "수정된 상품", price = 2000L)
                val savedProduct = createProduct(id = productId, name = "수정된 상품", price = 2000L)
                val slot = slot<Product>()

                justRun { validator.validate(request) }
                every { productRepository.findByIdNotDeleted(productId) } returns existingProduct
                every { existingProduct.update(request.name, request.price) } returns updatedProduct
                every { productRepository.save(capture(slot)) } returns savedProduct

                // when
                productUpdateService.updateProduct(productId, request)

                // then
                val capturedProduct = slot.captured
                capturedProduct shouldBe updatedProduct

                verify(exactly = 1) { validator.validate(request) }
                verify(exactly = 1) { productRepository.findByIdNotDeleted(productId) }
                verify(exactly = 1) { existingProduct.update(request.name, request.price) }
                verify(exactly = 1) { productRepository.save(updatedProduct) }
            }

            test("존재하지 않는 상품 ID로 업데이트 시 NoDataException이 발생해야 한다") {
                // given
                val productId = 999L
                val request = createProductUpdateRequest(name = "수정된 상품", price = 2000L)

                justRun { validator.validate(request) }
                every { productRepository.findByIdNotDeleted(productId) } returns null

                // when & then
                shouldThrow<NoDataException> {
                    productUpdateService.updateProduct(productId, request)
                }

                verify(exactly = 1) { validator.validate(request) }
                verify(exactly = 1) { productRepository.findByIdNotDeleted(productId) }
                verify(exactly = 0) { productRepository.save(any()) }
            }

            test("상품명이 빈 값인 경우 ValidationException이 발생해야 한다") {
                // given
                val productId = 1L
                val request = createProductUpdateRequest(name = "", price = 2000L)

                every { validator.validate(request) } throws ValidationException(ProductValidationMessages.PRODUCT_NAME_REQUIRED)

                // when & then
                shouldThrow<ValidationException> {
                    productUpdateService.updateProduct(productId, request)
                }

                verify(exactly = 1) { validator.validate(request) }
                verify(exactly = 0) { productRepository.findByIdNotDeleted(any()) }
                verify(exactly = 0) { productRepository.save(any()) }
            }

            test("상품 가격이 0 이하인 경우 ValidationException이 발생해야 한다") {
                // given
                val productId = 1L
                val request = createProductUpdateRequest(name = "테스트 상품", price = 0L)

                every { validator.validate(request) } throws ValidationException(ProductValidationMessages.PRODUCT_PRICE_MUST_BE_POSITIVE)

                // when & then
                shouldThrow<ValidationException> {
                    productUpdateService.updateProduct(productId, request)
                }

                verify(exactly = 1) { validator.validate(request) }
                verify(exactly = 0) { productRepository.findByIdNotDeleted(any()) }
                verify(exactly = 0) { productRepository.save(any()) }
            }

            test("상품 가격이 음수인 경우 ValidationException이 발생해야 한다") {
                // given
                val productId = 1L
                val request = createProductUpdateRequest(name = "테스트 상품", price = -1000L)

                every { validator.validate(request) } throws ValidationException(ProductValidationMessages.PRODUCT_PRICE_MUST_BE_POSITIVE)

                // when & then
                shouldThrow<ValidationException> {
                    productUpdateService.updateProduct(productId, request)
                }

                verify(exactly = 1) { validator.validate(request) }
                verify(exactly = 0) { productRepository.findByIdNotDeleted(any()) }
                verify(exactly = 0) { productRepository.save(any()) }
            }
        }
    }) {
    companion object {
        private fun createProductUpdateRequest(
            name: String = "테스트 상품",
            price: Long = 1000L,
        ): ProductUpdateRequest =
            ProductUpdateRequest(
                name = name,
                price = price,
            )

        private fun createProduct(
            id: Long = 1L,
            name: String = "테스트 상품",
            price: Long = 1000L,
            createdAt: LocalDateTime = LocalDateTime.now(),
            updatedAt: LocalDateTime = LocalDateTime.now(),
        ): Product =
            mockk {
                every { this@mockk.id } returns id
                every { this@mockk.name } returns name
                every { this@mockk.price } returns price
                every { this@mockk.createdAt } returns createdAt
                every { this@mockk.updatedAt } returns updatedAt
            }
    }
}
