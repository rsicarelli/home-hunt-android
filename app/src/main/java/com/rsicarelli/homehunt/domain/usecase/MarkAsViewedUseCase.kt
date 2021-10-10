package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import com.rsicarelli.homehunt.domain.repository.UserRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class MarkAsViewedUseCase(
    private val propertiesRepository: PropertyRepository,
    private val userRepository: UserRepository
) {

    //TODO refactor to flow
    operator fun invoke(request: Request) {
        propertiesRepository.markAsViewed(request.referenceId, userRepository.getUserId())
    }

    data class Request(
        val referenceId: String
    )
}