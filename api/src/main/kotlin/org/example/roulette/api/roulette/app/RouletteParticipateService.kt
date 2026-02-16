package org.example.roulette.api.roulette.app

import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.point.app.PointHistoryAppender
import org.example.roulette.api.point.domain.Point
import org.example.roulette.api.point.domain.PointType
import org.example.roulette.api.point.domain.ReferenceType
import org.example.roulette.api.roulette.domain.RouletteBudgetRepository
import org.example.roulette.api.roulette.domain.Roulette
import org.example.roulette.api.roulette.domain.RouletteHistory
import org.example.roulette.api.roulette.domain.RouletteHistoryRepository
import org.example.roulette.api.user.domain.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class RouletteParticipateService(
    private val userQueryService: UserQueryService,
    private val rouletteBudgetRepository: RouletteBudgetRepository,
    private val rouletteHistoryRepository: RouletteHistoryRepository,
    private val pointHistoryAppender: PointHistoryAppender,
) {
    fun participate(userId: Long): Point {
        val today = LocalDate.now()
        val user = userQueryService.getUser(userId)

        // 당일 참여 이력 검증
        if (existsRouletteHistory(userId, today)) {
            throw AlreadyParticipatedTodayException()
        }

        // 당일 예산 조회
        val rouletteBudget =
            rouletteBudgetRepository.findByBudgetDate(today)
                ?: throw EventPeriodException()

        val roulette = Roulette(rouletteBudget, user)

        // 예산 충분성 검증
        if (!roulette.canParticipate()) {
            throw InsufficientBudgetException()
        }

        val history = roulette.participate()

        rouletteBudgetRepository.save(roulette.getRouletteBudget())
        appendHistory(history)

        return Point(history.earnPoint)
    }

    private fun existsRouletteHistory(
        userId: Long,
        eventDate: LocalDate,
    ): Boolean = rouletteHistoryRepository.existsByUserIdAndEventDate(userId, eventDate)

    private fun appendHistory(rouletteHistory: RouletteHistory) {
        val saved = rouletteHistoryRepository.save(rouletteHistory)

        pointHistoryAppender.appendPointHistory(
            userId = saved.userId,
            amount = saved.earnPoint,
            type = PointType.EARN,
            referenceType = ReferenceType.ROULETTE,
            referenceId = saved.id,
        )
    }
}
