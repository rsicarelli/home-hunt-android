package com.rsicarelli.homehunt.presentation.filter

import com.rsicarelli.homehunt.domain.model.Filter

data class FilterState(
    val currentFilter: Filter? = null,
    val priceRange: ClosedFloatingPointRange<Float> = 0.0f..100f,
    val surfaceRange: ClosedFloatingPointRange<Float> = 0.0f..100f,
    val selectedDorms: List<Int?> = listOf(),
    val selectedBaths: List<Int?> = listOf()
)