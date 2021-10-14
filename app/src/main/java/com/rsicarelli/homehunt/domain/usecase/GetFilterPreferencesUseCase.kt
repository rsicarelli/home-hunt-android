package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.data.datasource.FilterLocalDataSource
import com.rsicarelli.homehunt.domain.model.SearchOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class GetFilterPreferencesUseCase(
    private val filterLocalDataSource: FilterLocalDataSource
) {

    operator fun invoke(): Flow<DataState<SearchOption>> =
        flow<DataState<SearchOption>> {
            emit(DataState.Data(filterLocalDataSource.get()))
        }.flowOn(Dispatchers.Default)
}