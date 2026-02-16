package org.example.roulette.api.roulette.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.longs.shouldBeGreaterThanOrEqual
import io.kotest.matchers.longs.shouldBeLessThanOrEqual
import io.kotest.matchers.shouldBe

class RoulettePointPolicyTest :
    FunSpec({
        val roulettePointPolicy = RoulettePointPolicy()

        context("랜덤 포인트 생성 테스트") {
            test("충분한 예산이 있을 때 최소/최대 범위 내의 포인트를 생성해야 한다") {
                // given
                val maxAllowedPoints = 5000L

                // when
                val result = roulettePointPolicy.generateRandomPoints(maxAllowedPoints)

                // then
                result.value shouldBeGreaterThanOrEqual 100L
                result.value shouldBeLessThanOrEqual 1000L
                result.value % 100 shouldBe 0L // 100의 배수여야 함
            }

            test("제한된 예산이 있을 때 예산 범위 내의 포인트를 생성해야 한다") {
                // given
                val maxAllowedPoints = 500L

                // when
                val result = roulettePointPolicy.generateRandomPoints(maxAllowedPoints)

                // then
                result.value shouldBeGreaterThanOrEqual 100L
                result.value shouldBeLessThanOrEqual 500L
                result.value % 100 shouldBe 0L // 100의 배수여야 함
            }

            test("최소 포인트와 동일한 예산일 때 최소 포인트를 생성해야 한다") {
                // given
                val maxAllowedPoints = 100L

                // when
                val result = roulettePointPolicy.generateRandomPoints(maxAllowedPoints)

                // then
                result.value shouldBe 100L
            }

            test("최소 포인트보다 적은 예산일 때 IllegalArgumentException이 발생해야 한다") {
                // given
                val maxAllowedPoints = 50L

                // when & then
                shouldThrow<IllegalArgumentException> {
                    roulettePointPolicy.generateRandomPoints(maxAllowedPoints)
                }
            }
        }
    })
