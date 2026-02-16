package org.example.roulette.api.roulette.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.roulette.api.point.domain.Point
import java.time.LocalDate

class RouletteTest :
    FunSpec({
        test("참여 가능한 예산이 있을 때 true를 반환해야 한다") {
            // given
            val rouletteBudget = createRouletteBudget(remainingBudget = 1000L)
            val roulettePointPolicy =
                mockk<RoulettePointPolicy> {
                    every { getMinPoint() } returns 100L
                }
            val roulette = Roulette(rouletteBudget, roulettePointPolicy)

            // when
            val result = roulette.canParticipate()

            // then
            result shouldBe true
        }

        test("참여 불가능한 예산일 때 false를 반환해야 한다") {
            // given
            val rouletteBudget = createRouletteBudget(remainingBudget = 50L)
            val roulettePointPolicy =
                mockk<RoulettePointPolicy> {
                    every { getMinPoint() } returns 100L
                }
            val roulette = Roulette(rouletteBudget, roulettePointPolicy)

            // when
            val result = roulette.canParticipate()

            // then
            result shouldBe false
        }

        test("룰렛 참여 시 포인트가 생성되고 예산이 차감되어야 한다") {
            // given
            val rouletteBudget = mockk<RouletteBudget>(relaxed = true)
            val roulettePointPolicy =
                mockk<RoulettePointPolicy> {
                    every { generateRandomPoints(1000L) } returns Point(500L)
                }
            val roulette = Roulette(rouletteBudget, roulettePointPolicy)

            every { rouletteBudget.remainingBudget } returns 1000L

            // when
            val result = roulette.participate()

            // then
            result.value shouldBe 500L
            verify(exactly = 1) { roulettePointPolicy.generateRandomPoints(1000L) }
            verify(exactly = 1) { rouletteBudget.deductBudget(Point(500L)) }
        }
    }) {
    companion object {
        private fun createRouletteBudget(
            id: Long = 1L,
            budgetDate: LocalDate = LocalDate.now(),
            totalBudget: Long = 10000L,
            remainingBudget: Long = 5000L,
        ): RouletteBudget =
            RouletteBudget(
                id = id,
                budgetDate = budgetDate,
                totalBudget = totalBudget,
                remainingBudget = remainingBudget,
            )
    }
}
