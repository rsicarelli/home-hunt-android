package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.domain.model.Filter
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PreviewFilterResultUseCase(
    private val propertyRepository: PropertyRepository,
    private val filterPropertiesUseCase: FilterPropertiesUseCase
) {
    @OptIn(FlowPreview::class)
    suspend operator fun invoke(request: Request): Flow<DataState<List<Property>>> =
        propertyRepository.getAll().flatMapConcat {
            filterPropertiesUseCase.invoke(
                FilterPropertiesUseCase.Request(
                    filter = request.filter,
                    properties = it
                )
            )
        }.flowOn(Dispatchers.IO)

    data class Request(
        val filter: Filter
    )
}
