package org.example.roulette.api.order.app

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.order.domain.Order
import org.example.roulette.api.order.domain.OrderNotCancelableException
import org.example.roulette.api.order.domain.OrderRepository
import org.example.roulette.api.order.domain.OrderStatus
import org.example.roulette.api.point.app.PointBalanceService
import java.time.LocalDateTime
import java.util.*

class OrderCancelServiceTest : FunSpec({
    val orderRepository = mockk<OrderRepository>()
    val pointBalanceService = mockk<PointBalanceService>()
    lateinit var orderCancelService: OrderCancelService

    beforeTest {
        clearMocks(orderRepository, pointBalanceService)
        orderCancelService = OrderCancelService(
            orderRepository = orderRepository,
            pointBalanceService = pointBalanceService,
        )
    }

    context("주문 취소 관련 테스트") {
        test("정상적인 주문 취소 시 주문이 성공적으로 취소되어야 한다") {
            // given
            val orderId = 1L
            val order = createOrder(id = orderId, status = OrderStatus.COMPLETED)
            val slot = slot<Order>()
            
            every { orderRepository.findById(orderId) } returns Optional.of(order)
            every { orderRepository.save(capture(slot)) } answers { firstArg() }
            justRun { pointBalanceService.addPoints(order.userId, order.priceAtOrder) }

            // when
            orderCancelService.cancelOrder(orderId)

            // then
            verify(exactly = 1) { orderRepository.findById(orderId) }
            verify(exactly = 1) { pointBalanceService.addPoints(order.userId, order.priceAtOrder) }
            verify(exactly = 1) { orderRepository.save(any()) }
        }

        test("존재하지 않는 주문을 취소하려고 하면 NoDataException이 발생해야 한다") {
            // given
            val orderId = 999L
            
            every { orderRepository.findById(orderId) } returns Optional.empty()

            // when & then
            shouldThrow<NoDataException> {
                orderCancelService.cancelOrder(orderId)
            }
            
            verify(exactly = 1) { orderRepository.findById(orderId) }
            verify(exactly = 0) { pointBalanceService.addPoints(any(), any()) }
            verify(exactly = 0) { orderRepository.save(any()) }
        }

        test("이미 취소된 주문을 취소하려고 하면 OrderNotCancelableException이 발생해야 한다") {
            // given
            val orderId = 1L
            val canceledOrder = createOrder(
                id = orderId, 
                status = OrderStatus.CANCELLED,
                canceledAt = LocalDateTime.now()
            )
            
            every { orderRepository.findById(orderId) } returns Optional.of(canceledOrder)

            // when & then
            shouldThrow<OrderNotCancelableException> {
                orderCancelService.cancelOrder(orderId)
            }
            
            verify(exactly = 1) { orderRepository.findById(orderId) }
            verify(exactly = 0) { pointBalanceService.addPoints(any(), any()) }
            verify(exactly = 0) { orderRepository.save(any()) }
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
            createdAt: LocalDateTime = LocalDateTime.now(),
            canceledAt: LocalDateTime? = null
        ): Order {
            return Order(
                id = id,
                userId = userId,
                productId = productId,
                priceAtOrder = priceAtOrder,
                status = status,
                createdAt = createdAt,
                canceledAt = canceledAt
            )
        }
    }
}