package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.domain.model.Property
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import timber.log.Timber

class GetFilteredPropertiesUseCase(
    private val getAllAllProperties: GetAllPropertiesUseCase,
    private val getFilterPreferences: GetFilterPreferencesUseCase,
    private val filterProperties: FilterPropertiesUseCase,
) {

    @OptIn(FlowPreview::class)
    suspend operator fun invoke(): Flow<DataState<List<Property>>> {
        return getAllAllProperties()
            .filter { it is DataState.Data }
            .mapNotNull { (it as DataState.Data).data }
            .onEach { Timber.d("Got properties ${it.size}") }
            .flatMapConcat { properties ->
                getFilterPreferences.invoke() //TODO find an alternative
                    .filter { it is DataState.Data }
                    .mapNotNull { (it as DataState.Data).data }
                    .onEach { Timber.d("Got current filter $it") }
                    .map { filter ->
                        Pair(properties, filter)
                    }
            }.flatMapConcat { listAndFilter ->
                filterProperties.invoke(
                    FilterPropertiesUseCase.Request(
                        properties = listAndFilter.first,
                        filter = listAndFilter.second
                    )
                )
            }
            .filter { it is DataState.Data }
    }


}