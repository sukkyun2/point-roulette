package org.example.roulette.api.point.app

import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.point.domain.Point
import org.example.roulette.api.point.domain.PointHistory
import org.example.roulette.api.point.domain.PointHistoryRepository
import org.example.roulette.api.point.domain.PointType
import org.example.roulette.api.point.domain.ReferenceType
import org.example.roulette.api.user.domain.User
import org.example.roulette.api.user.domain.UserRepository
import org.springframework.stereotype.Service

@Service
class PointBalanceService(
    private val pointHistoryRepository: PointHistoryRepository,
    private val userRepository: UserRepository,
) {
    fun deductPoints(
        userId: Long,
        amount: Long,
        referenceType: ReferenceType,
        referenceId: Long,
    ) {
        val user = getUser(userId)

        // User balance 차감
        user.deductBalance(Point(amount))
        userRepository.save(user)

        // PointHistory 기록
        val pointHistory =
            PointHistory(
                userId = userId,
                amount = amount,
                type = PointType.USE,
                referenceType = referenceType,
                referenceId = referenceId,
            )
        pointHistoryRepository.save(pointHistory)
    }

    private fun getUser(userId: Long): User = userRepository.findById(userId).orElseThrow { NoDataException() }
}
