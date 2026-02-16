package org.example.roulette.api.point.app

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.example.roulette.api.point.domain.Point
import org.example.roulette.api.point.domain.PointType
import org.example.roulette.api.user.domain.User
import org.example.roulette.api.user.domain.UserQueryService
import org.example.roulette.api.user.domain.UserRepository

class PointBalanceServiceTest : FunSpec({
    val userRepository = mockk<UserRepository>()
    val userQueryService = mockk<UserQueryService>()
    val pointHistoryAppender = mockk<PointHistoryAppender>()
    lateinit var pointBalanceService: PointBalanceService

    beforeTest {
        clearMocks(userRepository, userQueryService, pointHistoryAppender)
        pointBalanceService = PointBalanceService(
            userRepository = userRepository,
            userQueryService = userQueryService,
            pointHistoryAppender = pointHistoryAppender
        )
    }

    context("포인트 차감 관련 테스트") {
        test("정상적인 포인트 차감 시 사용자 잔액이 차감되고 이력이 저장되어야 한다") {
            // given
            val userId = 1L
            val amount = 1000L
            val user = createUser(userId = userId, balance = 5000L)
            
            every { userQueryService.getUser(userId) } returns user
            every { userRepository.save(any()) } returns user
            every { pointHistoryAppender.appendPointHistory(any(), any(), any()) } returns createPointHistory()

            // when
            pointBalanceService.deductPoints(userId, amount)

            // then
            verify(exactly = 1) { userQueryService.getUser(userId) }
            verify(exactly = 1) { user.deductBalance(Point(amount)) }
            verify(exactly = 1) { userRepository.save(user) }
            verify(exactly = 1) { 
                pointHistoryAppender.appendPointHistory(
                    userId = userId,
                    amount = amount,
                    type = PointType.USE
                )
            }
        }

        test("존재하지 않는 사용자 포인트 차감 시 예외가 발생해야 한다") {
            // given
            val userId = 999L
            val amount = 1000L
            every { userQueryService.getUser(userId) } throws NoSuchElementException("User not found")

            // when & then
            shouldThrow<NoSuchElementException> {
                pointBalanceService.deductPoints(userId, amount)
            }
            
            verify(exactly = 1) { userQueryService.getUser(userId) }
            verify(exactly = 0) { userRepository.save(any()) }
            verify(exactly = 0) { pointHistoryAppender.appendPointHistory(any(), any(), any()) }
        }

        test("잔액 부족 시 포인트 차감에서 예외가 발생해야 한다") {
            // given
            val userId = 1L
            val amount = 10000L
            val user = createUser(userId = userId, balance = 1000L)
            
            every { userQueryService.getUser(userId) } returns user
            every { user.deductBalance(Point(amount)) } throws IllegalArgumentException("잔액이 부족합니다")

            // when & then
            shouldThrow<IllegalArgumentException> {
                pointBalanceService.deductPoints(userId, amount)
            }
            
            verify(exactly = 1) { userQueryService.getUser(userId) }
            verify(exactly = 1) { user.deductBalance(Point(amount)) }
            verify(exactly = 0) { userRepository.save(any()) }
            verify(exactly = 0) { pointHistoryAppender.appendPointHistory(any(), any(), any()) }
        }
    }

    context("포인트 적립 관련 테스트") {
        test("정상적인 포인트 적립 시 사용자 잔액이 증가하고 이력이 저장되어야 한다") {
            // given
            val userId = 1L
            val amount = 1000L
            val user = createUser(userId = userId, balance = 5000L)
            
            every { userQueryService.getUser(userId) } returns user
            every { userRepository.save(any()) } returns user
            every { pointHistoryAppender.appendPointHistory(any(), any(), any()) } returns createPointHistory()

            // when
            pointBalanceService.addPoints(userId, amount)

            // then
            verify(exactly = 1) { userQueryService.getUser(userId) }
            verify(exactly = 1) { user.addBalance(Point(amount)) }
            verify(exactly = 1) { userRepository.save(user) }
            verify(exactly = 1) { 
                pointHistoryAppender.appendPointHistory(
                    userId = userId,
                    amount = amount,
                    type = PointType.REFUND
                )
            }
        }

        test("존재하지 않는 사용자 포인트 적립 시 예외가 발생해야 한다") {
            // given
            val userId = 999L
            val amount = 1000L
            every { userQueryService.getUser(userId) } throws NoSuchElementException("User not found")

            // when & then
            shouldThrow<NoSuchElementException> {
                pointBalanceService.addPoints(userId, amount)
            }
            
            verify(exactly = 1) { userQueryService.getUser(userId) }
            verify(exactly = 0) { userRepository.save(any()) }
            verify(exactly = 0) { pointHistoryAppender.appendPointHistory(any(), any(), any()) }
        }

        test("음수 금액으로 포인트 적립 시 예외가 발생해야 한다") {
            // given
            val userId = 1L
            val amount = -1000L
            val user = createUser(userId = userId, balance = 5000L)
            
            every { userQueryService.getUser(userId) } returns user
            every { user.addBalance(Point(amount)) } throws IllegalArgumentException("포인트는 음수일 수 없습니다.")

            // when & then
            shouldThrow<IllegalArgumentException> {
                pointBalanceService.addPoints(userId, amount)
            }
        }
    }
}) {
    companion object {
        private fun createUser(
            userId: Long = 1L,
            balance: Long = 1000L
        ): User {
            val user = mockk<User>()
            every { user.balance } returns balance
            justRun { user.deductBalance(any()) }
            justRun { user.addBalance(any()) }
            return user
        }

        private fun createPointHistory() = mockk<org.example.roulette.api.point.domain.PointHistory>()
    }
}