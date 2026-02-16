package org.example.roulette.api.order.app

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.roulette.api.order.domain.Order
import org.example.roulette.api.order.domain.OrderRepository
import org.example.roulette.api.order.domain.OrderStatus
import org.example.roulette.api.product.domain.Product
import org.example.roulette.api.product.domain.ProductRepository
import org.example.roulette.api.user.domain.User
import org.example.roulette.api.user.domain.UserQueryService
import java.time.LocalDateTime

class OrderHistoryQueryServiceTest : FunSpec({
    val orderRepository = mockk<OrderRepository>()
    val productRepository = mockk<ProductRepository>()
    val userQueryService = mockk<UserQueryService>()
    lateinit var orderHistoryQueryService: OrderHistoryQueryService

    beforeTest {
        clearMocks(orderRepository, productRepository, userQueryService)
        orderHistoryQueryService = OrderHistoryQueryService(
            orderRepository = orderRepository,
            productRepository = productRepository,
            userQueryService = userQueryService,
        )
    }

    context("사용자 주문 이력 조회 관련 테스트") {
        test("사용자의 주문 이력이 있는 경우 정상적으로 조회되어야 한다") {
            // given
            val userId = 1L
            val orders = listOf(
                createOrder(id = 1L, userId = userId, productId = 1L),
                createOrder(id = 2L, userId = userId, productId = 2L)
            )
            val products = listOf(
                createProduct(id = 1L, name = "Product 1"),
                createProduct(id = 2L, name = "Product 2")
            )
            
            every { orderRepository.findByUserIdOrderByCreatedAtDesc(userId) } returns orders
            every { productRepository.findAllById(listOf(1L, 2L)) } returns products

            // when
            val result = orderHistoryQueryService.getOrderHistory(userId)

            // then
            result shouldHaveSize 2
            result[0].id shouldBe 1L
            result[0].productName shouldBe "Product 1"
            result[1].id shouldBe 2L
            result[1].productName shouldBe "Product 2"
            
            verify(exactly = 1) { orderRepository.findByUserIdOrderByCreatedAtDesc(userId) }
            verify(exactly = 1) { productRepository.findAllById(listOf(1L, 2L)) }
        }

        test("존재하지 않는 상품 정보로 인한 예외 처리 테스트") {
            // given
            val userId = 1L
            val orders = listOf(createOrder(id = 1L, userId = userId, productId = 999L))
            
            every { orderRepository.findByUserIdOrderByCreatedAtDesc(userId) } returns orders
            every { productRepository.findAllById(listOf(999L)) } returns emptyList()

            // when & then
            shouldThrow<IllegalStateException> {
                orderHistoryQueryService.getOrderHistory(userId)
            }
            
            verify(exactly = 1) { orderRepository.findByUserIdOrderByCreatedAtDesc(userId) }
            verify(exactly = 1) { productRepository.findAllById(listOf(999L)) }
        }
    }

    context("전체 주문 이력 조회 관련 테스트") {
        test("상태 조건 없이 전체 주문 이력을 조회하면 모든 주문이 반환되어야 한다") {
            // given
            val orders = listOf(
                createOrder(id = 1L, userId = 1L, productId = 1L, status = OrderStatus.COMPLETED),
                createOrder(id = 2L, userId = 2L, productId = 2L, status = OrderStatus.CANCELLED)
            )
            val users = listOf(
                createUser(id = 1L, nickname = "User1"),
                createUser(id = 2L, nickname = "User2")
            )
            val products = listOf(
                createProduct(id = 1L, name = "Product 1"),
                createProduct(id = 2L, name = "Product 2")
            )

            every { orderRepository.findAllOrderByCreatedAtDesc() } returns orders
            every { userQueryService.findAllByIds(listOf(1L, 2L)) } returns users
            every { productRepository.findAllById(listOf(1L, 2L)) } returns products

            // when
            val result = orderHistoryQueryService.getOrderHistories(null)

            // then
            result.orders shouldHaveSize 2
            result.orders[0].id shouldBe 1L
            result.orders[0].nickName shouldBe "User1"
            result.orders[1].id shouldBe 2L
            result.orders[1].nickName shouldBe "User2"

            verify(exactly = 1) { orderRepository.findAllOrderByCreatedAtDesc() }
            verify(exactly = 0) { orderRepository.findByStatusOrderByCreatedAtDesc(any()) }
        }

        test("존재하지 않는 사용자 정보로 인한 예외 처리 테스트") {
            // given
            val orders = listOf(createOrder(id = 1L, userId = 999L, productId = 1L))
            val products = listOf(createProduct(id = 1L))

            every { orderRepository.findAllOrderByCreatedAtDesc() } returns orders
            every { userQueryService.findAllByIds(listOf(999L)) } returns emptyList()
            every { productRepository.findAllById(listOf(1L)) } returns products

            // when & then
            shouldThrow<IllegalStateException> {
                orderHistoryQueryService.getOrderHistories(null)
            }
        }
    }
}) {
    companion object {
        private fun createOrder(
            id: Long = 1L,
            userId: Long = 1L,
            productId: Long = 1L,
            priceAtOrder: Long = 1000L,
            status: OrderStatus = OrderStatus.COMPLETED,
            createdAt: LocalDateTime = LocalDateTime.now()
        ): Order {
            return Order(
                id = id,
                userId = userId,
                productId = productId,
                priceAtOrder = priceAtOrder,
                status = status,
                createdAt = createdAt
            )
        }

        private fun createUser(
            id: Long = 1L,
            nickname: String = "testUser"
        ): User {
            return mockk {
                every { this@mockk.id } returns id
                every { this@mockk.nickname } returns nickname
            }
        }

        private fun createProduct(
            id: Long = 1L,
            name: String = "Test Product"
        ): Product {
            return mockk {
                every { this@mockk.id } returns id
                every { this@mockk.name } returns name
            }
        }
    }
}