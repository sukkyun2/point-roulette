package org.example.roulette.api.order.app

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.order.api.OrderRequest
import org.example.roulette.api.order.domain.Order
import org.example.roulette.api.order.domain.OrderRepository
import org.example.roulette.api.order.domain.OrderStatus
import org.example.roulette.api.point.app.PointBalanceService
import org.example.roulette.api.point.domain.Point
import org.example.roulette.api.product.domain.Product
import org.example.roulette.api.product.domain.ProductRepository
import org.example.roulette.api.user.domain.User
import org.example.roulette.api.user.domain.UserQueryService
import java.time.LocalDateTime
import java.util.Optional

class OrderServiceTest :
    FunSpec({
        val orderRepository = mockk<OrderRepository>()
        val productRepository = mockk<ProductRepository>()
        val userQueryService = mockk<UserQueryService>()
        val pointBalanceService = mockk<PointBalanceService>()
        lateinit var orderService: OrderService

        beforeTest {
            clearMocks(orderRepository, productRepository, userQueryService, pointBalanceService)
            orderService =
                OrderService(
                    orderRepository = orderRepository,
                    productRepository = productRepository,
                    userQueryService = userQueryService,
                    pointBalanceService = pointBalanceService,
                )
        }

        context("주문 생성 관련 테스트") {
            test("정상적인 주문 생성 시 주문이 성공적으로 생성되어야 한다") {
                // given
                val userId = 1L
                val request = createOrderRequest(productId = 2L)
                val user = createUser(id = userId, balance = 5000)
                val product = createProduct(id = 2L, price = 1000L)
                val expectedOrder = createOrder(userId = userId, productId = 2L, priceAtOrder = 1000L)

                every { userQueryService.getUser(userId) } returns user
                every { productRepository.findById(2L) } returns Optional.of(product)
                every { orderRepository.save(any()) } returns expectedOrder
                justRun { pointBalanceService.deductPoints(userId, 1000L) }

                // when
                val result = orderService.createOrder(userId, request)

                // then
                result shouldBe expectedOrder
                verify(exactly = 1) { userQueryService.getUser(userId) }
                verify(exactly = 1) { productRepository.findById(2L) }
                verify(exactly = 1) { orderRepository.save(any()) }
                verify(exactly = 1) { pointBalanceService.deductPoints(userId, 1000L) }
            }

            test("포인트가 부족한 경우 InsufficientPointException이 발생해야 한다") {
                // given
                val userId = 1L
                val request = createOrderRequest(productId = 2L)
                val user = createUser(id = userId, balance = 500)
                val product = createProduct(id = 2L, price = 1000L)

                every { userQueryService.getUser(userId) } returns user
                every { productRepository.findById(2L) } returns Optional.of(product)

                // when & then
                shouldThrow<InsufficientPointException> {
                    orderService.createOrder(userId, request)
                }

                verify(exactly = 1) { userQueryService.getUser(userId) }
                verify(exactly = 1) { productRepository.findById(2L) }
                verify(exactly = 0) { orderRepository.save(any()) }
                verify(exactly = 0) { pointBalanceService.deductPoints(any(), any()) }
            }

            test("존재하지 않는 상품으로 주문 시 NoDataException이 발생해야 한다") {
                // given
                val userId = 1L
                val request = createOrderRequest(productId = 999L)
                val user = createUser(id = userId, balance = 5000)

                every { userQueryService.getUser(userId) } returns user
                every { productRepository.findById(999L) } returns Optional.empty()

                // when & then
                shouldThrow<NoDataException> {
                    orderService.createOrder(userId, request)
                }

                verify(exactly = 1) { userQueryService.getUser(userId) }
                verify(exactly = 1) { productRepository.findById(999L) }
                verify(exactly = 0) { orderRepository.save(any()) }
                verify(exactly = 0) { pointBalanceService.deductPoints(any(), any()) }
            }
        }
    }) {
    companion object {
        private fun createOrderRequest(productId: Long = 1L): OrderRequest = OrderRequest(productId = productId)

        private fun createOrder(
            id: Long = 1L,
            userId: Long = 1L,
            productId: Long = 1L,
            priceAtOrder: Long = 1000L,
            status: OrderStatus = OrderStatus.COMPLETED,
            createdAt: LocalDateTime = LocalDateTime.now(),
        ): Order =
            Order(
                id = id,
                userId = userId,
                productId = productId,
                priceAtOrder = priceAtOrder,
                status = status,
                createdAt = createdAt,
            )

        private fun createUser(
            id: Long = 1L,
            nickname: String = "testUser",
            balance: Long = 1000,
        ): User =
            mockk {
                every { this@mockk.id } returns id
                every { this@mockk.nickname } returns nickname
                every { this@mockk.balance } returns balance
                every { canDeduct(any()) } answers { this@mockk.balance >= firstArg<Point>().value }
            }

        private fun createProduct(
            id: Long = 1L,
            name: String = "Test Product",
            price: Long = 1000L,
        ): Product =
            mockk {
                every { this@mockk.id } returns id
                every { this@mockk.name } returns name
                every { this@mockk.price } returns price
            }
    }
}
