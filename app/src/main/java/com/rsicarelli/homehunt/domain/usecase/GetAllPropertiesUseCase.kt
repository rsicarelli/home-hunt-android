package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import com.rsicarelli.homehunt.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

class GetAllPropertiesUseCase(
    private val propertiesRepository: PropertyRepository,
) {
    operator fun invoke(): Flow<DataState<List<Property>>> =
        flow<DataState<List<Property>>> {
            propertiesRepository.getActiveProperties()
                .onStart {
                    emit(DataState.Loading(ProgressBarState.Loading))
                }
                .collect {
                    emit(DataState.Data(it))
                }
        }.flowOn(Dispatchers.IO)
}