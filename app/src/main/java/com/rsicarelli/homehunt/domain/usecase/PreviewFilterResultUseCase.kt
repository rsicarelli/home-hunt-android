package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.domain.model.SearchOption
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import com.rsicarelli.homehunt.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn

class PreviewFilterResultUseCase(
    private val propertyRepository: PropertyRepository,
    private val userRepository: UserRepository,
    private val filterPropertiesUseCase: FilterPropertiesUseCase
) {
    @OptIn(FlowPreview::class)
    suspend operator fun invoke(request: Request): Flow<DataState<List<Property>>> =
        propertyRepository.getNewProperties(userRepository.getUserId()).flatMapConcat {
            filterPropertiesUseCase.invoke(
                FilterPropertiesUseCase.Request(
                    searchOption = request.searchOption,
                    properties = it
                )
            )
        }.flowOn(Dispatchers.IO)

    data class Request(
        val searchOption: SearchOption
    )
}
