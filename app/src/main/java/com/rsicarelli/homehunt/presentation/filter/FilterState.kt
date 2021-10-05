package com.rsicarelli.homehunt.presentation.filter

import com.rsicarelli.homehunt.domain.model.Filter

data class FilterState(
    val currentFilter: Filter? = null,
    val priceRange: ClosedFloatingPointRange<Float> = 0.0f..999999F,
    val surfaceRange: ClosedFloatingPointRange<Float> = 0.0f..999999f,
    val selectedDorms: List<Int> = listOf(),
    val selectedBaths: List<Int> = listOf(),
    val previewResultCount: Int = 0
) {
    fun toFilter(): Filter {
        return Filter(
            priceRange = Pair(priceRange.start.toDouble(), priceRange.endInclusive.toDouble()),
            surfaceRange = Pair(surfaceRange.start.toInt(), surfaceRange.endInclusive.toInt()),
            dormCount = selectedDorms,
            bathCount = selectedBaths,
        )
    }
}