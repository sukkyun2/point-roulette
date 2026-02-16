package org.example.roulette.api.roulette.app

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.example.roulette.api.roulette.domain.RouletteBudget
import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
import org.example.roulette.api.roulette.domain.RouletteHistoryRepository
import org.example.roulette.api.roulette.domain.RouletteStatus
import org.example.roulette.api.user.domain.User
import org.example.roulette.api.user.domain.UserRepository
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest(properties = ["spring.profiles.active=test"])
class RouletteParticipateServiceIntegrationTest(
    private val rouletteBudgetRepository: RouletteBudgetRepository,
    private val rouletteHistoryRepository: RouletteHistoryRepository,
    private val userRepository: UserRepository,
    private val service: RouletteParticipateService,
) : FunSpec({

    beforeTest {
        rouletteHistoryRepository.deleteAll()
        rouletteBudgetRepository.deleteAll()
        userRepository.deleteAll()
    }

    test("100포인트 예산에 10명이 동시에 참여하면 한 명만 성공해야한다") {
        val userCount = 10
        val totalBudget = 100L
        val fixedDate = LocalDate.now()

        //Given
        val users = (1..userCount)
            .map { User(nickname = "user$it", balance = 0L) }
        userRepository.saveAll(users)

        rouletteBudgetRepository.save(
            RouletteBudget(
                budgetDate = fixedDate,
                totalBudget = totalBudget,
                remainingBudget = totalBudget
            )
        )

        val executor = Executors.newFixedThreadPool(20)
        val latch = CountDownLatch(userCount)

        // When
        val futures = users.map { user ->
            CompletableFuture.runAsync({
                latch.countDown()
                latch.await()
                try {
                    service.participate(user.id)
                } catch (_: Exception) {}
            }, executor)
        }

        CompletableFuture.allOf(*futures.toTypedArray()).join()
        executor.shutdown()

        // Then
        val histories = rouletteHistoryRepository
            .findByEventDateAndStatus(fixedDate, RouletteStatus.SUCCESS)

        val budget = rouletteBudgetRepository.findByBudgetDate(fixedDate) ?: error("해당 날짜의 예산이 없습니다: $fixedDate")

        histories.size shouldBe 1
        budget.remainingBudget shouldBe 0L
    }
})