package org.example.roulette.api.roulette.app

import org.example.roulette.api.common.app.NoDataException
import org.example.roulette.api.roulette.domain.RouletteHistoryRepository
import org.example.roulette.api.roulette.domain.RouletteStatus
import org.example.roulette.api.user.domain.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RouletteHistoryListQueryService(
    private val rouletteHistoryRepository: RouletteHistoryRepository,
    private val userRepository: UserRepository,
) {
    fun getParticipationHistories(status: RouletteStatus?): List<RouletteParticipationHistoryItem> {
        val participations = rouletteHistoryRepository.findAllByStatus(status)
        val userIds = participations.map { it.userId }.distinct()
        val users = userRepository.findAllById(userIds).associateBy { it.id }

        return participations.map { participation ->
            val user =
                users[participation.userId]
                    ?: throw NoDataException()

            RouletteParticipationHistoryItem(
                id = participation.id,
                nickName = user.nickname,
                earnPoint = participation.earnPoint,
                status = participation.status,
                createdAt = participation.createdAt,
            )
        }
    }
}
