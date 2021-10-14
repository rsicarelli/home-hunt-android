package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.domain.model.SearchOption
import com.rsicarelli.homehunt.domain.model.Property
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FilterPropertiesUseCase {
    operator fun invoke(request: Request): Flow<DataState<List<Property>>> =
        flow<DataState<List<Property>>> {
            val (searchOption, properties) = request

            val result = searchOption.applyFilter(properties)

            emit(DataState.Data(result))
        }.flowOn(Dispatchers.IO)

    data class Request(
        val searchOption: SearchOption,
        val properties: List<Property>
    )
}
