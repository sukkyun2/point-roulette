package org.example.roulette.api.order.app

import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.order.domain.Order
import org.example.roulette.api.order.domain.OrderRepository
import org.example.roulette.api.point.app.PointHistoryAppender
import org.example.roulette.api.point.domain.Point
import org.example.roulette.api.point.domain.PointType
import org.example.roulette.api.point.domain.ReferenceType
import org.example.roulette.api.user.domain.UserQueryService
import org.example.roulette.api.user.domain.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrderCancelService(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val userQueryService: UserQueryService,
    private val pointHistoryAppender: PointHistoryAppender,
) {
    fun cancelOrder(orderId: Long) {
        val order = getOrder(orderId)

        // 주문 취소
        order.cancel()
        orderRepository.save(order)

        // 포인트 환불
        refundPoints(order.userId, order.priceAtOrder, orderId)
    }

    private fun getOrder(orderId: Long): Order =
        orderRepository.findById(orderId).orElseThrow {
            NoDataException()
        }

    private fun refundPoints(
        userId: Long,
        amount: Long,
        orderId: Long,
    ) {
        val user = userQueryService.getUser(userId)

        // User balance 증가
        user.addBalance(Point(amount))
        userRepository.save(user)

        // PointHistory 기록
        pointHistoryAppender.appendPointHistory(
            userId = userId,
            amount = amount,
            type = PointType.REFUND,
            referenceType = ReferenceType.ORDER,
            referenceId = orderId,
        )
    }
}
