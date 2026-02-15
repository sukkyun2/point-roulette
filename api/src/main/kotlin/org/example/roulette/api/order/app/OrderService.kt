package org.example.roulette.api.order.app

import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.order.domain.Order
import org.example.roulette.api.order.domain.OrderRepository
import org.example.roulette.api.order.domain.OrderStatus
import org.example.roulette.api.point.app.PointBalanceService
import org.example.roulette.api.point.domain.ReferenceType
import org.example.roulette.api.product.domain.Product
import org.example.roulette.api.product.domain.ProductRepository
import org.example.roulette.api.user.domain.User
import org.example.roulette.api.user.domain.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrderService(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val pointBalanceService: PointBalanceService,
) {
    fun createOrder(
        userId: Long,
        productId: Long,
    ): Order {
        val user = getUser(userId)
        val product = getProduct(productId)

        if (user.balance < product.price) {
            throw InsufficientPointException()
        }

        val order =
            Order(
                userId = userId,
                productId = product.id,
                priceAtOrder = product.price,
                status = OrderStatus.COMPLETED,
            )

        val savedOrder = orderRepository.save(order)

        pointBalanceService.deductPoints(
            userId = userId,
            amount = product.price,
            referenceType = ReferenceType.ORDER,
            referenceId = savedOrder.id,
        )

        return savedOrder
    }

    private fun getUser(userId: Long): User = userRepository.findById(userId).orElseThrow { NoDataException() }

    private fun getProduct(productId: Long): Product =
        productRepository.findById(productId).orElseThrow {
            NoDataException()
        }
}
