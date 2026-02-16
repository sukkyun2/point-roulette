package org.example.roulette.api.point.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PointTypeTest : FunSpec({
    
    context("PointType isPositive 테스트") {
        test("EARN 타입은 양수 타입이어야 한다") {
            // given
            val pointType = PointType.EARN

            // when
            val isPositive = pointType.isPositive()

            // then
            isPositive shouldBe true
        }

        test("USE 타입은 음수 타입이어야 한다") {
            // given
            val pointType = PointType.USE

            // when
            val isPositive = pointType.isPositive()

            // then
            isPositive shouldBe false
        }

        test("REFUND 타입은 양수 타입이어야 한다") {
            // given
            val pointType = PointType.REFUND

            // when
            val isPositive = pointType.isPositive()

            // then
            isPositive shouldBe true
        }
    }
})