package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class GetAllPropertiesUseCase(
    private val propertiesRepository: PropertyRepository
) {
    suspend operator fun invoke(): Flow<DataState<List<Property>>> =
        flow<DataState<List<Property>>> {
            propertiesRepository.getAll()
                .onStart {
                    emit(DataState.Loading(ProgressBarState.Loading))
                }
                .collect {
                    delay(200)
                    emit(DataState.Data(it))
                }
        }
}