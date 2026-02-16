package org.example.roulette.api.user.domain

import org.example.roulette.api.common.app.NoDataException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserQueryService(
    private val userRepository: UserRepository,
) {
    fun findByNickname(nickname: String): User? = userRepository.findByNickname(nickname)

    fun getUser(id: Long): User = userRepository.findByIdOrNull(id) ?: throw NoDataException()

    fun findAllByIds(ids: List<Long>): List<User> = userRepository.findAllById(ids)
}
