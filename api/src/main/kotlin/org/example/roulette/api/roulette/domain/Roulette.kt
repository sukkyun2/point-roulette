package org.example.roulette.api.roulette.domain

import org.example.roulette.api.point.domain.Point
import org.example.roulette.api.user.domain.User
import java.time.LocalDate

class Roulette(
    private val rouletteBudget: RouletteBudget,
    private val user: User,
    private val roulettePointPolicy: RoulettePointPolicy = RoulettePointPolicy(),
    private val eventDate: LocalDate = LocalDate.now(),
) {
    fun canParticipate(): Boolean = rouletteBudget.canParticipate(roulettePointPolicy.getMinPoint())

    fun participate(): RouletteHistory {
        // 랜덤 포인트 생성
        val earnedPoint = roulettePointPolicy.generateRandomPoints(rouletteBudget.remainingBudget)

        // 예산 차감
        rouletteBudget.deductBudget(earnedPoint)

        // 사용자 포인트 적립
        user.addBalance(earnedPoint)

        return createHistory(earnedPoint)
    }

    private fun createHistory(earnedPoint: Point): RouletteHistory =
        RouletteHistory(
            userId = user.id,
            eventDate = eventDate,
            earnPoint = earnedPoint.value,
        )

    fun getRouletteBudget() = rouletteBudget
}
