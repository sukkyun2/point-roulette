package org.example.roulette.api.user.domain

import org.springframework.stereotype.Service

@Service
class UserQueryService(
    private val userRepository: UserRepository
) {

    fun findByNickname(nickname: String): User? {
        return userRepository.findByNickname(nickname)
    }
}