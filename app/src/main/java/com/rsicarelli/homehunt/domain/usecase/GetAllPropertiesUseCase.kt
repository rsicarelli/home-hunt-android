package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.UseCase
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import com.rsicarelli.homehunt.domain.usecase.GetAllPropertiesUseCase.Outcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllPropertiesUseCase(
    private val propertiesRepository: PropertyRepository,
) : UseCase<Unit, Outcome> {

    sealed class Outcome {
        data class Data(val properties: List<Property>) : Outcome()
    }

    override fun invoke(request: Unit): Flow<Outcome> =
        propertiesRepository.getActiveProperties().map { Outcome.Data(it) }

}