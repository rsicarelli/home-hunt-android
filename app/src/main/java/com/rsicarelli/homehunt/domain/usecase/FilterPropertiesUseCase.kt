package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.domain.model.Filter
import com.rsicarelli.homehunt.domain.model.Property
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

//TODO Super ugly, make it more performative
class FilterPropertiesUseCase(
) {
    suspend operator fun invoke(request: Request): Flow<DataState<List<Property>>> =
        flow<DataState<List<Property>>> {
            val (filter, properties) = request

            val result = properties.filter {
                val range = if (filter.priceRange.second == filter.maxPrice) {
                    filter.priceRange.first..99999.0
                } else {
                    filter.priceRange.first..filter.priceRange.second
                }

                it.price in range
            }.filter {
                val range = if (filter.surfaceRange.second == filter.maxSurface) {
                    filter.surfaceRange.first..99999
                } else {
                    filter.surfaceRange.first..filter.surfaceRange.second
                }

                it.surface in range
            }.filter {
                if (filter.dormSelection.isNotEmpty()) {
                    it.dormCount in filter.dormSelection
                } else {
                    true
                }
            }.filter {
                if (filter.bathSelection.isNotEmpty()) {
                    it.bathCount in filter.bathSelection
                } else {
                    true
                }
            }

            emit(DataState.Data(result))
        }

    data class Request(
        val filter: Filter,
        val properties: List<Property>
    )
}
