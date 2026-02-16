package org.example.roulette.api.order.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.assertions.throwables.shouldThrow
import java.time.LocalDateTime

class OrderTest : FunSpec({
    
    context("Order 생성 테스트") {
        test("정상적인 값으로 생성 시 올바르게 초기화되어야 한다") {
            // given
            val userId = 1L
            val productId = 2L
            val priceAtOrder = 1000L

            // when
            val order = Order.create(
                userId = userId,
                productId = productId,
                priceAtOrder = priceAtOrder
            )

            // then
            order.userId shouldBe userId
            order.productId shouldBe productId
            order.priceAtOrder shouldBe priceAtOrder
            order.status shouldBe OrderStatus.COMPLETED
            order.canceledAt shouldBe null
        }
    }

    context("Order 취소 로직 테스트") {
        test("완료 상태의 주문을 취소하면 정상적으로 취소되어야 한다") {
            // given
            val order = createOrder(status = OrderStatus.COMPLETED)

            // when
            order.cancel()

            // then
            order.status shouldBe OrderStatus.CANCELLED
            order.canceledAt shouldNotBe null
        }

        test("이미 취소된 주문을 취소하려고 하면 예외가 발생해야 한다") {
            // given
            val order = createOrder(status = OrderStatus.CANCELLED, canceledAt = LocalDateTime.now())

            // when & then
            shouldThrow<OrderNotCancelableException> {
                order.cancel()
            }
        }
    }

    context("Order 상태 확인 테스트") {
        test("취소된 주문은 isCancelled가 true를 반환해야 한다") {
            // given
            val order = createOrder(status = OrderStatus.CANCELLED)

            // when
            val result = order.isCancelled()

            // then
            result shouldBe true
        }

        test("완료 상태의 주문은 isCancelled가 false를 반환해야 한다") {
            // given
            val order = createOrder(status = OrderStatus.COMPLETED)

            // when
            val result = order.isCancelled()

            // then
            result shouldBe false
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