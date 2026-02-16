package org.example.roulette.api.roulette.app

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.example.roulette.api.common.app.ValidationException
import org.example.roulette.api.roulette.domain.RouletteBudget
import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
import java.time.LocalDate

class RouletteBudgetCreateServiceTest :
    FunSpec({
        val rouletteBudgetRepository = mockk<RouletteBudgetRepository>()
        val rouletteBudgetCreateValidator = mockk<RouletteBudgetCreateValidator>()
        lateinit var rouletteBudgetCreateService: RouletteBudgetCreateService

        beforeTest {
            clearMocks(rouletteBudgetRepository, rouletteBudgetCreateValidator)
            rouletteBudgetCreateService =
                RouletteBudgetCreateService(
                    rouletteBudgetRepository = rouletteBudgetRepository,
                    rouletteBudgetCreateValidator = rouletteBudgetCreateValidator,
                )
        }

        context("룰렛 예산 생성 테스트") {
            test("정상적인 예산 생성 시 예산이 생성되어야 한다") {
                // given
                val request = createRouletteBudgetCreateRequest()

                justRun { rouletteBudgetCreateValidator.validate(request.budgetDate, request.totalBudget) }
                every { rouletteBudgetRepository.findByBudgetDate(request.budgetDate) } returns null
                every { rouletteBudgetRepository.save(any()) } returns createRouletteBudget()

                // when
                rouletteBudgetCreateService.createRouletteBudget(request)

                // then
                verify(exactly = 1) { rouletteBudgetCreateValidator.validate(request.budgetDate, request.totalBudget) }
                verify(exactly = 1) { rouletteBudgetRepository.findByBudgetDate(request.budgetDate) }
                verify(exactly = 1) { rouletteBudgetRepository.save(any()) }
            }

            test("이미 같은 날짜의 예산이 존재하는 경우 ValidationException이 발생해야 한다") {
                // given
                val request = createRouletteBudgetCreateRequest()
                val existingBudget = createRouletteBudget()

                justRun { rouletteBudgetCreateValidator.validate(request.budgetDate, request.totalBudget) }
                every { rouletteBudgetRepository.findByBudgetDate(request.budgetDate) } returns existingBudget

                // when & then
                shouldThrow<ValidationException> {
                    rouletteBudgetCreateService.createRouletteBudget(request)
                }

                verify(exactly = 1) { rouletteBudgetCreateValidator.validate(request.budgetDate, request.totalBudget) }
                verify(exactly = 1) { rouletteBudgetRepository.findByBudgetDate(request.budgetDate) }
                verify(exactly = 0) { rouletteBudgetRepository.save(any()) }
            }

            test("유효성 검사 실패 시 ValidationException이 발생해야 한다") {
                // given
                val request = createRouletteBudgetCreateRequest()

                every { rouletteBudgetCreateValidator.validate(request.budgetDate, request.totalBudget) } throws ValidationException("유효성 검사 실패")

                // when & then
                shouldThrow<ValidationException> {
                    rouletteBudgetCreateService.createRouletteBudget(request)
                }

                verify(exactly = 1) { rouletteBudgetCreateValidator.validate(request.budgetDate, request.totalBudget) }
                verify(exactly = 0) { rouletteBudgetRepository.findByBudgetDate(any()) }
                verify(exactly = 0) { rouletteBudgetRepository.save(any()) }
            }
        }
    }) {
    companion object {
        private fun createRouletteBudgetCreateRequest(
            budgetDate: LocalDate = LocalDate.now(),
            totalBudget: Long = 10000L,
        ): RouletteBudgetCreateRequest =
            RouletteBudgetCreateRequest(
                budgetDate = budgetDate,
                totalBudget = totalBudget,
            )

        private fun createRouletteBudget(
            id: Long = 1L,
            budgetDate: LocalDate = LocalDate.now(),
            totalBudget: Long = 10000L,
            remainingBudget: Long = 10000L,
        ): RouletteBudget =
            RouletteBudget(
                id = id,
                budgetDate = budgetDate,
                totalBudget = totalBudget,
                remainingBudget = remainingBudget,
            )
    }
}
