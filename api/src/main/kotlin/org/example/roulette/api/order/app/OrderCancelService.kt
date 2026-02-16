package org.example.roulette.api.order.app

import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.order.domain.Order
import org.example.roulette.api.order.domain.OrderRepository
import org.example.roulette.api.point.app.PointBalanceService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrderCancelService(
    private val orderRepository: OrderRepository,
    private val pointBalanceService: PointBalanceService,
) {
    fun cancelOrder(orderId: Long) {
        val order = getOrder(orderId)

        order.cancel()
        pointBalanceService.addPoints(
            userId = order.userId,
            amount = order.priceAtOrder,
        )

        orderRepository.save(order)
    }

    private fun getOrder(orderId: Long): Order =
        orderRepository.findById(orderId).orElseThrow {
            NoDataException()
        }
}
