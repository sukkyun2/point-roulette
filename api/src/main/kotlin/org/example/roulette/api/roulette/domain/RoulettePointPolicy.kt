package org.example.roulette.api.roulette.domain

import org.example.roulette.api.point.domain.Point
import org.springframework.stereotype.Component
import kotlin.random.Random

class RoulettePointPolicy {
    companion object {
        private const val MIN_POINT = 100L
        private const val MAX_POINT = 1000L
    }

    fun getMinPoint(): Long = MIN_POINT

    fun generateRandomPoints(maxAllowedPoints: Long): Point {
        val actualMaxPoints = minOf(MAX_POINT, maxAllowedPoints)
        require(actualMaxPoints >= MIN_POINT) { "룰렛 참여를 위해서는 최소 ${MIN_POINT}포인트가 필요합니다" }

        val maxSteps = actualMaxPoints / 100
        val minSteps = MIN_POINT / 100
        val randomStep = Random.Default.nextLong(minSteps, maxSteps + 1)

        return Point(randomStep * 100)
    }
}
