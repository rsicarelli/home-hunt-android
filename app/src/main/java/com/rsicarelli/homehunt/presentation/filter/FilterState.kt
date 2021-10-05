package com.rsicarelli.homehunt.presentation.filter

import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.domain.model.Filter

data class FilterState(
    val priceRange: ClosedFloatingPointRange<Float> = 0.0f..999999F,
    val surfaceRange: ClosedFloatingPointRange<Float> = 0.0f..999999f,
    val selectedDorms: List<Int> = listOf(),
    val selectedBaths: List<Int> = listOf(),
    val previewResultCount: Int = 0,
    val uiEvent: UiEvent = UiEvent.Idle,
) {
    fun toFilter() = Filter(
        priceRange = Pair(priceRange.start.toDouble(), priceRange.endInclusive.toDouble()),
        surfaceRange = Pair(surfaceRange.start.toInt(), surfaceRange.endInclusive.toInt()),
        dormSelection = selectedDorms,
        bathSelection = selectedBaths,
    )

    fun fromFilter(filter: Filter) = FilterState(
        priceRange = filter.priceRange.first.toFloat()..filter.priceRange.second.toFloat(),
        surfaceRange = filter.surfaceRange.first.toFloat()..filter.surfaceRange.second.toFloat(),
        selectedBaths = filter.bathSelection,
        selectedDorms = filter.dormSelection
    )
}