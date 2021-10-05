package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.data.datasource.FilterLocalDataSource
import com.rsicarelli.homehunt.domain.model.Filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SaveFilterPreferencesUseCase(
    private val filterLocalDataSource: FilterLocalDataSource
) {

    suspend operator fun invoke(request: Request): Flow<DataState<Boolean>> =
        flow<DataState<Boolean>> {
            filterLocalDataSource.save(request.filter)
            emit(DataState.Data(true))
        }

    data class Request(
        val filter: Filter
    )
}