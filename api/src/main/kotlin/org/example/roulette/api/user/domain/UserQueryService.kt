package org.example.roulette.api.user.domain

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserQueryService(
    private val userRepository: UserRepository,
) {
    fun findByNickname(nickname: String): User? = userRepository.findByNickname(nickname)
    
    fun findById(id: Long): User? = userRepository.findByIdOrNull(id)
}
