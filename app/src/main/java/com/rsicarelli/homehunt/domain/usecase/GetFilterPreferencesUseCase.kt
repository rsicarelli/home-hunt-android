package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.data.datasource.FilterLocalDataSource
import com.rsicarelli.homehunt.domain.model.Filter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetFilterPreferencesUseCase(
    private val filterLocalDataSource: FilterLocalDataSource
) {

    suspend operator fun invoke(): Flow<DataState<Filter>> =
        flow<DataState<Filter>> {
            emit(DataState.Data(filterLocalDataSource.get()))
        }
}