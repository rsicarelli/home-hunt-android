package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.domain.repository.PropertyRepository

class ToggleFavouriteUseCase(
    private val propertyRepository: PropertyRepository
) {
    //TODO refactor to flow
    operator fun invoke(request: Request) {
        propertyRepository.toggleFavourite(request.referenceId, request.isFavourited)
    }

    data class Request(
        val referenceId: String,
        val isFavourited: Boolean
    )
}