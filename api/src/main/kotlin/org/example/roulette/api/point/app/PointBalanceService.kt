package org.example.roulette.api.point.app

import org.example.roulette.api.point.domain.Point
import org.example.roulette.api.point.domain.PointType
import org.example.roulette.api.user.domain.UserQueryService
import org.example.roulette.api.user.domain.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PointBalanceService(
    private val userRepository: UserRepository,
    private val userQueryService: UserQueryService,
    private val pointHistoryAppender: PointHistoryAppender,
) {
    fun deductPoints(
        userId: Long,
        amount: Long,
    ) {
        val user = userQueryService.getUser(userId)

        user.deductBalance(Point(amount))
        userRepository.save(user)

        pointHistoryAppender.appendPointHistory(
            userId = userId,
            amount = amount,
            type = PointType.USE,
        )
    }

    fun addPoints(
        userId: Long,
        amount: Long,
    ) {
        val user = userQueryService.getUser(userId)

        user.addBalance(Point(amount))
        userRepository.save(user)

        pointHistoryAppender.appendPointHistory(
            userId = userId,
            amount = amount,
            type = PointType.REFUND,
        )
    }
}
