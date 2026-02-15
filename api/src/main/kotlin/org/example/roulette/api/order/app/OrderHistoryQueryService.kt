package org.example.roulette.api.order.app

import org.example.roulette.api.order.api.OrderHistoryItemResponse
import org.example.roulette.api.order.domain.OrderRepository
import org.example.roulette.api.product.domain.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class OrderHistoryQueryService(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
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
}
