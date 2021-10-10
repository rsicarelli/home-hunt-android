package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class GetSinglePropertyUseCase(
    private val propertiesRepository: PropertyRepository
) {
    suspend operator fun invoke(request: Request): Flow<DataState<Property>> =
        flow<DataState<Property>> {
            propertiesRepository.getBy(request.referenceId)
                .onStart {
                    emit(DataState.Loading(ProgressBarState.Loading))
                }
                .collect {
                    delay(200)
                    emit(DataState.Data(it))
                }
        }.flowOn(Dispatchers.Default)

    data class Request(
        val referenceId: String
    )
}