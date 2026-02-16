package org.example.roulette.api.order.app

import org.example.roulette.api.order.domain.Order
import org.example.roulette.api.order.domain.OrderRepository
import org.example.roulette.api.order.domain.OrderStatus
import org.example.roulette.api.product.domain.Product
import org.example.roulette.api.product.domain.ProductRepository
import org.example.roulette.api.user.domain.User
import org.example.roulette.api.user.domain.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class OrderHistoryQueryService(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val userQueryService: UserQueryService,
) {
    fun getOrderHistory(userId: Long): List<OrderHistoryItemResponse> {
        val orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId)
        val productIds = orders.map { it.productId }
        val products = productRepository.findAllById(productIds).associateBy { it.id }

        return orders.map { order ->
            val product = products[order.productId] ?: throw IllegalStateException("Product not found")
            OrderHistoryItemResponse(
                id = order.id,
                productName = product.name,
                deductedPoints = order.priceAtOrder,
                status = order.status,
                createdAt = order.createdAt,
            )
        }
    }

    fun getOrderHistories(status: OrderStatus?): OrderListQueryWithUserResponse {
        val orders = getOrdersByStatus(status)
        val userIds = orders.map { it.userId }
        val productIds = orders.map { it.productId }

        val users = userQueryService.findAllByIds(userIds).associateBy { it.id }
        val products = productRepository.findAllById(productIds).associateBy { it.id }

        val orderItems = convertToOrderItems(orders, users, products)
        return OrderListQueryWithUserResponse(orderItems)
    }

    private fun getOrdersByStatus(status: OrderStatus?) =
        if (status != null) {
            orderRepository.findByStatusOrderByCreatedAtDesc(status)
        } else {
            orderRepository.findAllOrderByCreatedAtDesc()
        }

    private fun convertToOrderItems(
        orders: List<Order>,
        users: Map<Long, User>,
        products: Map<Long, Product>,
    ) = orders.map { order ->
        val user = users[order.userId] ?: throw IllegalStateException("User not found")
        val product = products[order.productId] ?: throw IllegalStateException("Product not found")

        OrderItemResponse(
            id = order.id,
            nickName = user.nickname,
            productName = product.name,
            deductedPoints = order.priceAtOrder,
            status = order.status,
            createdAt = order.createdAt,
        )
    }
}
