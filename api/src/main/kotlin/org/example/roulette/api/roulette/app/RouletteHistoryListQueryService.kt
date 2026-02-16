package org.example.roulette.api.roulette.app

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
    fun getParticipationHistories(status: RouletteStatus?): RouletteParticipationHistoryListQueryResponse {
        val participations = rouletteHistoryRepository.findAllByStatus(status)
        val userIds = participations.map { it.userId }.distinct()
        val users = userRepository.findAllById(userIds).associateBy { it.id }

        val participationItems = participations.map { participation ->
            val user = users[participation.userId]
            RouletteParticipationHistoryItem(
                id = participation.id,
                nickName = user?.nickname ?: "Unknown",
                earnPoint = participation.earnPoint,
                status = participation.status,
                createdAt = participation.createdAt,
            )
        }

        return RouletteParticipationHistoryListQueryResponse(participationItems)
    }
}