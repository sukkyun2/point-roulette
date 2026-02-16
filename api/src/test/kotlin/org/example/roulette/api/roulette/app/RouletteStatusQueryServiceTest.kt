package org.example.roulette.api.roulette.app

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.roulette.domain.RouletteBudget
import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
import org.example.roulette.api.roulette.domain.RouletteHistoryRepository
import org.example.roulette.api.roulette.domain.RouletteStatus
import java.time.LocalDate

class RouletteStatusQueryServiceTest :
    FunSpec({
        val rouletteBudgetRepository = mockk<RouletteBudgetRepository>()
        val rouletteHistoryRepository = mockk<RouletteHistoryRepository>()
        lateinit var rouletteStatusQueryService: RouletteStatusQueryService

        beforeTest {
            clearMocks(rouletteBudgetRepository, rouletteHistoryRepository)
            rouletteStatusQueryService =
                RouletteStatusQueryService(
                    rouletteBudgetRepository = rouletteBudgetRepository,
                    rouletteHistoryRepository = rouletteHistoryRepository,
                )
        }

        context("룰렛 상태 조회 테스트") {
            test("정상적인 상태 조회 시 오늘 참여하지 않은 경우 응답을 반환해야 한다") {
                // given
                val userId = 1L
                val today = LocalDate.now()
                val rouletteBudget = createRouletteBudget(remainingBudget = 5000L)

                every { rouletteHistoryRepository.existsByUserIdAndEventDateAndStatus(userId, today, RouletteStatus.SUCCESS) } returns false
                every { rouletteBudgetRepository.findByBudgetDate(today) } returns rouletteBudget

                // when
                val result = rouletteStatusQueryService.getStatus(userId)

                // then
                result.hasParticipatedToday shouldBe false
                result.remainingBudget shouldBe 5000L
                verify(exactly = 1) { rouletteHistoryRepository.existsByUserIdAndEventDateAndStatus(userId, today, RouletteStatus.SUCCESS) }
                verify(exactly = 1) { rouletteBudgetRepository.findByBudgetDate(today) }
            }

            test("정상적인 상태 조회 시 오늘 참여한 경우 응답을 반환해야 한다") {
                // given
                val userId = 1L
                val today = LocalDate.now()
                val rouletteBudget = createRouletteBudget(remainingBudget = 3000L)

                every { rouletteHistoryRepository.existsByUserIdAndEventDateAndStatus(userId, today, RouletteStatus.SUCCESS) } returns true
                every { rouletteBudgetRepository.findByBudgetDate(today) } returns rouletteBudget

                // when
                val result = rouletteStatusQueryService.getStatus(userId)

                // then
                result.hasParticipatedToday shouldBe true
                result.remainingBudget shouldBe 3000L
                verify(exactly = 1) { rouletteHistoryRepository.existsByUserIdAndEventDateAndStatus(userId, today, RouletteStatus.SUCCESS) }
                verify(exactly = 1) { rouletteBudgetRepository.findByBudgetDate(today) }
            }

            test("오늘 날짜의 룰렛 예산이 없는 경우 NoDataException이 발생해야 한다") {
                // given
                val userId = 1L
                val today = LocalDate.now()

                every { rouletteHistoryRepository.existsByUserIdAndEventDateAndStatus(userId, today, RouletteStatus.SUCCESS) } returns false
                every { rouletteBudgetRepository.findByBudgetDate(today) } returns null

                // when & then
                shouldThrow<NoDataException> {
                    rouletteStatusQueryService.getStatus(userId)
                }

                verify(exactly = 1) { rouletteHistoryRepository.existsByUserIdAndEventDateAndStatus(userId, today, RouletteStatus.SUCCESS) }
                verify(exactly = 1) { rouletteBudgetRepository.findByBudgetDate(today) }
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
