package org.example.roulette.api.roulette.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.roulette.api.point.domain.Point
import org.example.roulette.api.roulette.app.InsufficientBudgetException
import java.time.LocalDate

class RouletteBudgetTest :
    FunSpec({
        context("예산 차감 테스트") {
            test("충분한 예산이 있을 때 정상적으로 차감되어야 한다") {
                // given
                val rouletteBudget = createRouletteBudget(remainingBudget = 1000L)
                val point = Point(500L)

                // when
                rouletteBudget.deductBudget(point)

                // then
                rouletteBudget.remainingBudget shouldBe 500L
            }

            test("예산이 부족할 때 InsufficientBudgetException이 발생해야 한다") {
                // given
                val rouletteBudget = createRouletteBudget(remainingBudget = 300L)
                val point = Point(500L)

                // when & then
                shouldThrow<InsufficientBudgetException> {
                    rouletteBudget.deductBudget(point)
                }
            }
        }

        context("참여 가능 여부 테스트") {
            test("최소 요구 포인트보다 많은 예산이 있을 때 true를 반환해야 한다") {
                // given
                val rouletteBudget = createRouletteBudget(remainingBudget = 1000L)

                // when
                val result = rouletteBudget.canParticipate(500L)

                // then
                result shouldBe true
            }

            test("최소 요구 포인트보다 적은 예산이 있을 때 false를 반환해야 한다") {
                // given
                val rouletteBudget = createRouletteBudget(remainingBudget = 300L)

                // when
                val result = rouletteBudget.canParticipate(500L)

                // then
                result shouldBe false
            }

            test("최소 요구 포인트와 동일한 예산이 있을 때 true를 반환해야 한다") {
                // given
                val rouletteBudget = createRouletteBudget(remainingBudget = 500L)

                // when
                val result = rouletteBudget.canParticipate(500L)

                // then
                result shouldBe true
            }
        }

        context("예산 추가 테스트") {
            test("예산 추가 시 남은 예산이 증가해야 한다") {
                // given
                val rouletteBudget = createRouletteBudget(remainingBudget = 1000L)
                val point = Point(500L)

                // when
                rouletteBudget.addBudget(point)

                // then
                rouletteBudget.remainingBudget shouldBe 1500L
            }
        }

        context("예산 업데이트 테스트") {
            test("사용한 예산을 고려하여 정상적으로 업데이트되어야 한다") {
                // given
                val rouletteBudget = createRouletteBudget(totalBudget = 10000L, remainingBudget = 7000L)
                val newTotalBudget = 15000L

                // when
                rouletteBudget.updateBudget(newTotalBudget)

                // then
                rouletteBudget.totalBudget shouldBe 15000L
                rouletteBudget.remainingBudget shouldBe 12000L // 15000 - (10000 - 7000) = 12000
            }

            test("사용한 예산보다 낮은 금액으로 수정 시 InsufficientBudgetException이 발생해야 한다") {
                // given
                val rouletteBudget = createRouletteBudget(totalBudget = 10000L, remainingBudget = 7000L)
                val newTotalBudget = 2000L // 사용한 예산(3000L)보다 작음

                // when & then
                shouldThrow<InsufficientBudgetException> {
                    rouletteBudget.updateBudget(newTotalBudget)
                }
            }

            test("사용한 예산과 동일한 금액으로 수정 시 남은 예산이 0이 되어야 한다") {
                // given
                val rouletteBudget = createRouletteBudget(totalBudget = 10000L, remainingBudget = 7000L)
                val newTotalBudget = 3000L // 사용한 예산과 동일

                // when
                rouletteBudget.updateBudget(newTotalBudget)

                // then
                rouletteBudget.totalBudget shouldBe 3000L
                rouletteBudget.remainingBudget shouldBe 0L
            }
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
