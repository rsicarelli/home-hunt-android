package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSinglePropertyUseCase(
    private val propertiesRepository: PropertyRepository
) {
    operator fun invoke(request: Request): Flow<Property> =
        propertiesRepository.getActiveProperties().map { properties ->
            properties.first { it.reference == request.referenceId }
        }

    class Request(
        val referenceId: String
    )
}