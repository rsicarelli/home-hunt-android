package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFavouritedPropertiesUseCase(
    private val propertiesRepository: PropertyRepository
) {
    operator fun invoke(): Flow<List<Property>> =
        propertiesRepository.getActiveProperties()
            .map { properties -> properties.filter { it.isFavourited } }

}