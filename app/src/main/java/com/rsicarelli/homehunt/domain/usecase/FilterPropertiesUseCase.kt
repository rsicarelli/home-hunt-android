package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.domain.model.SearchOption
import com.rsicarelli.homehunt.domain.model.Property
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FilterPropertiesUseCase {
    suspend operator fun invoke(request: Request): Flow<DataState<List<Property>>> =
        flow<DataState<List<Property>>> {
            val (searchOption, properties) = request

            val result = searchOption.applyFilter(properties)

            emit(DataState.Data(result))
        }

    data class Request(
        val searchOption: SearchOption,
        val properties: List<Property>
    )
}
