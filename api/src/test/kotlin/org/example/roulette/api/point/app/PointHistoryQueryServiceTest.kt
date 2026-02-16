package org.example.roulette.api.point.app

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.roulette.api.point.domain.PointHistory
import org.example.roulette.api.point.domain.PointHistoryRepository
import org.example.roulette.api.point.domain.PointType
import org.example.roulette.api.user.domain.User
import org.example.roulette.api.user.domain.UserQueryService
import java.time.LocalDateTime

class PointHistoryQueryServiceTest :
    FunSpec({
        val pointHistoryRepository = mockk<PointHistoryRepository>()
        val userQueryService = mockk<UserQueryService>()
        lateinit var pointHistoryQueryService: PointHistoryQueryService

        beforeTest {
            clearMocks(pointHistoryRepository, userQueryService)
            pointHistoryQueryService =
                PointHistoryQueryService(
                    pointHistoryRepository = pointHistoryRepository,
                    userQueryService = userQueryService,
                )
        }

        context("포인트 이력 조회 관련 테스트") {
            test("정상적인 이력 조회 시 올바른 응답을 반환해야 한다") {
                // given
                val userId = 1L
                val pointHistories =
                    listOf(
                        createPointHistory(id = 1, amount = 1000L, type = PointType.EARN),
                        createPointHistory(id = 2, amount = 500L, type = PointType.USE),
                    )
                val user = createUser(balance = 2000L)
                val expiringSoonPoints =
                    listOf(
                        createPointHistory(amount = 300L, type = PointType.EARN, createdAt = LocalDateTime.now().minusDays(25)),
                    )

                every { pointHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId) } returns pointHistories
                every { userQueryService.getUser(userId) } returns user
                every {
                    pointHistoryRepository.findExpiringSoonPointsByUserId(
                        userId = userId,
                        from = any(),
                        to = any(),
                    )
                } returns expiringSoonPoints

                // when
                val result = pointHistoryQueryService.getHistories(userId)

                // then
                result.balance.available shouldBe 2000L
                result.balance.expiringSoon shouldBe 300L
                result.history.size shouldBe 2
                result.history[0].id shouldBe 1
                result.history[0].amount shouldBe 1000L
                result.history[0].type shouldBe PointType.EARN
                result.history[1].id shouldBe 2
                result.history[1].amount shouldBe 500L
                result.history[1].type shouldBe PointType.USE

                verify(exactly = 1) { pointHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId) }
                verify(exactly = 1) { userQueryService.getUser(userId) }
                verify(exactly = 1) { pointHistoryRepository.findExpiringSoonPointsByUserId(any(), any(), any()) }
            }

            test("이력이 없는 사용자 조회 시 빈 리스트를 반환해야 한다") {
                // given
                val userId = 1L
                val user = createUser(balance = 0L)

                every { pointHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId) } returns emptyList()
                every { userQueryService.getUser(userId) } returns user
                every {
                    pointHistoryRepository.findExpiringSoonPointsByUserId(any(), any(), any())
                } returns emptyList()

                // when
                val result = pointHistoryQueryService.getHistories(userId)

                // then
                result.balance.available shouldBe 0L
                result.balance.expiringSoon shouldBe 0L
                result.history shouldBe emptyList()

                verify(exactly = 1) { pointHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId) }
                verify(exactly = 1) { userQueryService.getUser(userId) }
            }

            test("존재하지 않는 사용자 조회 시 예외가 발생해야 한다") {
                // given
                val userId = 999L
                every { pointHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId) } returns emptyList()
                every { userQueryService.getUser(userId) } throws NoSuchElementException("User not found")

                // when & then
                shouldThrow<NoSuchElementException> {
                    pointHistoryQueryService.getHistories(userId)
                }

                verify(exactly = 1) { pointHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId) }
                verify(exactly = 1) { userQueryService.getUser(userId) }
            }
        }

        context("포인트 이력 단일 조회 테스트") {
            test("사용자 포인트 이력 조회 시 올바른 데이터를 반환해야 한다") {
                // given
                val userId = 1L
                val expectedHistories =
                    listOf(
                        createPointHistory(id = 3, amount = 2000L, type = PointType.REFUND),
                        createPointHistory(id = 1, amount = 1000L, type = PointType.EARN),
                    )

                every { pointHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId) } returns expectedHistories

                // when
                val result = pointHistoryQueryService.getPointHistory(userId)

                // then
                result shouldBe expectedHistories
                verify(exactly = 1) { pointHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId) }
            }
        }

        context("잔액 조회 테스트") {
            test("사용 가능 잔액 조회 시 사용자 잔액을 반환해야 한다") {
                // given
                val userId = 1L
                val user = createUser(balance = 5000L)

                every { userQueryService.getUser(userId) } returns user

                // when
                val result = pointHistoryQueryService.calculateAvailableBalance(userId)

                // then
                result shouldBe 5000L
                verify(exactly = 1) { userQueryService.getUser(userId) }
            }
        }

        context("곧 만료될 포인트 조회 테스트") {
            test("곧 만료될 포인트 조회 시 올바른 금액을 반환해야 한다") {
                // given
                val userId = 1L
                val now = LocalDateTime.now()
                val expiringSoonPoints =
                    listOf(
                        createPointHistory(amount = 1000L, type = PointType.EARN, createdAt = now.minusDays(25)),
                        createPointHistory(amount = 500L, type = PointType.EARN, createdAt = now.minusDays(28)),
                        createPointHistory(amount = 200L, type = PointType.USE, createdAt = now.minusDays(26)), // USE는 제외
                    )

                every {
                    pointHistoryRepository.findExpiringSoonPointsByUserId(
                        userId = userId,
                        from = any(),
                        to = any(),
                    )
                } returns expiringSoonPoints

                // when
                val result = pointHistoryQueryService.calculateExpiringSoonBalance(userId)

                // then
                result shouldBe 1500L // EARN 타입만 합산 (1000 + 500)
                verify(exactly = 1) { pointHistoryRepository.findExpiringSoonPointsByUserId(any(), any(), any()) }
            }

            test("만료된 포인트가 포함된 경우 만료되지 않은 포인트만 계산해야 한다") {
                // given
                val userId = 1L
                val now = LocalDateTime.now()
                val expiringSoonPoints =
                    listOf(
                        createPointHistory(amount = 1000L, type = PointType.EARN, createdAt = now.minusDays(25)), // 만료 안됨
                        createPointHistory(amount = 500L, type = PointType.EARN, createdAt = now.minusDays(35)), // 만료됨
                    )

                every {
                    pointHistoryRepository.findExpiringSoonPointsByUserId(any(), any(), any())
                } returns expiringSoonPoints

                // when
                val result = pointHistoryQueryService.calculateExpiringSoonBalance(userId)

                // then
                result shouldBe 1000L // 만료되지 않은 것만 합산
                verify(exactly = 1) { pointHistoryRepository.findExpiringSoonPointsByUserId(any(), any(), any()) }
            }

            test("곧 만료될 포인트가 없는 경우 0을 반환해야 한다") {
                // given
                val userId = 1L

                every {
                    pointHistoryRepository.findExpiringSoonPointsByUserId(any(), any(), any())
                } returns emptyList()

                // when
                val result = pointHistoryQueryService.calculateExpiringSoonBalance(userId)

                // then
                result shouldBe 0L
                verify(exactly = 1) { pointHistoryRepository.findExpiringSoonPointsByUserId(any(), any(), any()) }
            }
        }
    }) {
    companion object {
        private fun createPointHistory(
            id: Long = 1L,
            userId: Long = 1L,
            amount: Long = 1000L,
            type: PointType = PointType.EARN,
            createdAt: LocalDateTime = LocalDateTime.now(),
        ): PointHistory {
            val pointHistory = mockk<PointHistory>()
            every { pointHistory.id } returns id
            every { pointHistory.userId } returns userId
            every { pointHistory.amount } returns amount
            every { pointHistory.type } returns type
            every { pointHistory.createdAt } returns createdAt
            every { pointHistory.getExpiresAt() } returns if (type == PointType.EARN) createdAt.plusDays(30) else null
            every { pointHistory.isExpired() } returns
                if (type == PointType.EARN) {
                    createdAt.plusDays(30).isBefore(LocalDateTime.now())
                } else {
                    false
                }
            return pointHistory
        }

        private fun createUser(balance: Long = 1000L): User {
            val user = mockk<User>()
            every { user.balance } returns balance
            return user
        }
    }
}
