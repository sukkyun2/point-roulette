package org.example.roulette.api.roulette.domain

import org.example.roulette.api.point.domain.Point

class Roulette(
    private val rouletteBudget: RouletteBudget,
    private val roulettePointPolicy: RoulettePointPolicy = RoulettePointPolicy(),
) {
    fun canParticipate(): Boolean = rouletteBudget.canParticipate(roulettePointPolicy.getMinPoint())

    fun participate(): Point {
        val earnedPoint = roulettePointPolicy.generateRandomPoints(rouletteBudget.remainingBudget)
        rouletteBudget.deductBudget(earnedPoint)

        return earnedPoint
    }

    fun getRouletteBudget() = rouletteBudget
}
