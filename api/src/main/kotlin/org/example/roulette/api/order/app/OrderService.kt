package org.example.roulette.api.order.app

import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.order.api.OrderRequest
import org.example.roulette.api.order.domain.Order
import org.example.roulette.api.order.domain.OrderRepository
import org.example.roulette.api.point.app.PointBalanceService
import org.example.roulette.api.point.domain.Point
import org.example.roulette.api.point.domain.ReferenceType
import org.example.roulette.api.product.domain.Product
import org.example.roulette.api.product.domain.ProductRepository
import org.example.roulette.api.user.domain.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrderService(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val userQueryService: UserQueryService,
    private val pointBalanceService: PointBalanceService,
) {
    fun createOrder(
        userId: Long,
        request: OrderRequest,
    ): Order {
        val user = userQueryService.getUser(userId)
        val product = getProduct(request.productId)

        if (!user.canDeduct(Point(product.price))) {
            throw InsufficientPointException()
        }

        val order = Order.create(
            userId = userId,
            productId = product.id,
            priceAtOrder = product.price,
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

    private fun getProduct(productId: Long): Product =
        productRepository.findById(productId).orElseThrow {
            NoDataException()
        }
}
