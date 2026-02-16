package org.example.roulette.api.roulette.app

import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.point.app.PointHistoryAppender
import org.example.roulette.api.point.domain.Point
import org.example.roulette.api.point.domain.PointType
import org.example.roulette.api.point.domain.ReferenceType
import org.example.roulette.api.roulette.domain.DailyBudgetRepository
import org.example.roulette.api.roulette.domain.RouletteHistoryRepository
import org.example.roulette.api.roulette.domain.RouletteStatus
import org.example.roulette.api.user.domain.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RouletteCancelService(
    private val rouletteHistoryRepository: RouletteHistoryRepository,
    private val dailyBudgetRepository: DailyBudgetRepository,
    private val userQueryService: UserQueryService,
    private val pointHistoryAppender: PointHistoryAppender,
) {
    fun cancelParticipation(rouletteHistoryId: Long) {
        val rouletteHistory =
            rouletteHistoryRepository
                .findById(rouletteHistoryId)
                .orElseThrow { NoDataException("룰렛 참여 기록을 찾을 수 없습니다.") }

        if (rouletteHistory.status == RouletteStatus.CANCELED) {
            throw IllegalStateException("이미 취소된 룰렛 참여입니다.")
        }

        val user = userQueryService.getUser(rouletteHistory.userId)
        val earnedPoint = Point(rouletteHistory.earnPoint)

        // 사용자 잔액에서 포인트 회수 (음수 허용)
        user.deductBalanceAllowNegative(earnedPoint)

        // 예산에 포인트 재반영
        val dailyBudget =
            dailyBudgetRepository.findByBudgetDate(rouletteHistory.eventDate)
                ?: throw NoDataException("해당 일자의 예산 정보를 찾을 수 없습니다.")

        dailyBudget.addBudget(earnedPoint)

        // 룰렛 참여 상태를 취소로 변경
        val updatedHistory =
            rouletteHistory.copy(
                status = RouletteStatus.CANCELED,
            )

        dailyBudgetRepository.save(dailyBudget)
        rouletteHistoryRepository.save(updatedHistory)

        // 포인트 회수 히스토리 기록
        pointHistoryAppender.appendPointHistory(
            userId = user.id,
            amount = earnedPoint.value,
            type = PointType.USE,
            referenceType = ReferenceType.ROULETTE_CANCEL,
            referenceId = rouletteHistoryId,
        )
    }
}
