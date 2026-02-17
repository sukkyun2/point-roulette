package org.example.roulette.api.product.app

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.roulette.api.product.domain.Product
import org.example.roulette.api.product.domain.ProductRepository
import java.time.LocalDateTime

class ProductListQueryServiceTest :
    FunSpec({
        val productRepository = mockk<ProductRepository>()
        lateinit var productListQueryService: ProductListQueryService

        beforeTest {
            clearMocks(productRepository)
            productListQueryService =
                ProductListQueryService(
                    productRepository = productRepository,
                )
        }

        context("상품 목록 조회 관련 테스트") {
            test("상품 목록이 존재할 때 정상적으로 조회되어야 한다") {
                // given
                val products =
                    listOf(
                        createProduct(id = 1L, name = "상품1", price = 1000L),
                        createProduct(id = 2L, name = "상품2", price = 2000L),
                        createProduct(id = 3L, name = "상품3", price = 3000L),
                    )
                val expectedResponses =
                    listOf(
                        ProductListQueryResponse(id = 1L, name = "상품1", price = 1000L),
                        ProductListQueryResponse(id = 2L, name = "상품2", price = 2000L),
                        ProductListQueryResponse(id = 3L, name = "상품3", price = 3000L),
                    )

                every { productRepository.findAllNotDeleted() } returns products

                // when
                val result = productListQueryService.findAll()

                // then
                result shouldHaveSize 3
                result shouldBe expectedResponses

                verify(exactly = 1) { productRepository.findAllNotDeleted() }
            }

            test("상품 목록이 비어있을 때 빈 리스트가 반환되어야 한다") {
                // given
                val products = emptyList<Product>()

                every { productRepository.findAllNotDeleted() } returns products

                // when
                val result = productListQueryService.findAll()

                // then
                result.shouldBeEmpty()

                verify(exactly = 1) { productRepository.findAllNotDeleted() }
            }
        }
    }) {
    companion object {
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
