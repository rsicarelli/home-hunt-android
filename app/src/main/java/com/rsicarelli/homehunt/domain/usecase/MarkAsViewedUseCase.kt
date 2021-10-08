package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import com.rsicarelli.homehunt.domain.repository.UserRepository

class MarkAsViewedUseCase(
    private val propertiesRepository: PropertyRepository,
    private val userRepository: UserRepository
) {

    operator fun invoke(request: Request) {
        propertiesRepository.markAsViewed(request.referenceId, userRepository.getUserId())
    }

    data class Request(
        val referenceId: String
    )
}