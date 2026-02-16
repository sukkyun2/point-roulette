package org.example.roulette.api.point.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class PointHistoryTest : FunSpec({
    context("PointHistory 도메인 로직 테스트") {
        test("EARN 타입 포인트의 만료일 계산 - 생성일로부터 30일 후여야 한다") {
            // given
            val createdAt = LocalDateTime.of(2024, 1, 15, 10, 30)
            val pointHistory = createPointHistory(
                type = PointType.EARN,
                createdAt = createdAt
            )

            // when
            val expiresAt = pointHistory.getExpiresAt()

            // then
            expiresAt shouldBe createdAt.plusDays(30)
        }

        test("USE 타입 포인트의 만료일 - null이어야 한다") {
            // given
            val pointHistory = createPointHistory(type = PointType.USE)

            // when
            val expiresAt = pointHistory.getExpiresAt()

            // then
            expiresAt.shouldBeNull()
        }

        test("REFUND 타입 포인트의 만료일 - null이어야 한다") {
            // given
            val pointHistory = createPointHistory(type = PointType.REFUND)

            // when
            val expiresAt = pointHistory.getExpiresAt()

            // then
            expiresAt.shouldBeNull()
        }

        test("EARN 타입 포인트 만료 확인 - 30일 경과 후 만료되어야 한다") {
            // given
            val pastDate = LocalDateTime.now().minusDays(31)
            val pointHistory = createPointHistory(
                type = PointType.EARN,
                createdAt = pastDate
            )

            // when
            val isExpired = pointHistory.isExpired()

            // then
            isExpired shouldBe true
        }

        test("EARN 타입 포인트 만료 확인 - 30일 이내는 만료되지 않아야 한다") {
            // given
            val recentDate = LocalDateTime.now().minusDays(29)
            val pointHistory = createPointHistory(
                type = PointType.EARN,
                createdAt = recentDate
            )

            // when
            val isExpired = pointHistory.isExpired()

            // then
            isExpired shouldBe false
        }

        test("USE 타입 포인트 만료 확인 - 만료되지 않아야 한다") {
            // given
            val oldDate = LocalDateTime.now().minusDays(100)
            val pointHistory = createPointHistory(
                type = PointType.USE,
                createdAt = oldDate
            )

            // when
            val isExpired = pointHistory.isExpired()

            // then
            isExpired shouldBe false
        }
    }
}) {
    companion object {
        private fun createPointHistory(
            userId: Long = 1L,
            amount: Long = 1000L,
            type: PointType = PointType.EARN,
            createdAt: LocalDateTime = LocalDateTime.now()
        ): PointHistory {
            return PointHistory(
                userId = userId,
                amount = amount,
                type = type,
                createdAt = createdAt
            )
        }
    }
}