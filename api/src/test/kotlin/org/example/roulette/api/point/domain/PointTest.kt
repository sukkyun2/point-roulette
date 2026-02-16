package org.example.roulette.api.point.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PointTest : FunSpec({
    
    context("Point 객체 생성 테스트") {
        test("음수 값으로 생성 시 예외가 발생해야 한다") {
            // given
            val negativeValue = -1L

            // when & then
            shouldThrow<IllegalArgumentException> {
                Point(negativeValue)
            }
        }
    }

    context("Point 값 객체 테스트") {
        test("동등성 비교 - 같은 값의 객체는 동일해야 한다") {
            // given
            val point1 = Point(5000L)
            val point2 = Point(5000L)

            // when & then
            point1 shouldBe point2
            point1.hashCode() shouldBe point2.hashCode()
        }

        test("불변성 - Point 객체는 data class로 불변이어야 한다") {
            // given
            val originalValue = 1000L
            val point = Point(originalValue)

            // when
            val newPoint = point.copy(value = 2000L)

            // then
            point.value shouldBe originalValue  // 원본 불변
            newPoint.value shouldBe 2000L      // 새 객체 생성
        }
    }
})