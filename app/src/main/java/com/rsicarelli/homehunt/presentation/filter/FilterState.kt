package com.rsicarelli.homehunt.presentation.filter

import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.domain.model.SearchOption

data class FilterState(
    val priceRange: ClosedFloatingPointRange<Float> = 0.0f..999999F,
    val surfaceRange: ClosedFloatingPointRange<Float> = 0.0f..999999f,
    val selectedDorms: List<Int> = listOf(),
    val selectedBaths: List<Int> = listOf(),
    val seenOnly: Boolean = false,
    val notSeenOnly: Boolean = false,
    val seenAndNotSeen: Boolean = true,
    val longTermOnly: Boolean = false,
    val previewResultCount: Int = 0,
    val showReserved: Boolean = false,
    val showRented: Boolean = false,
    val uiEvent: UiEvent = UiEvent.Idle,
) {
    fun toFilter() = SearchOption(
        priceRange = Pair(priceRange.start.toDouble(), priceRange.endInclusive.toDouble()),
        surfaceRange = Pair(surfaceRange.start.toInt(), surfaceRange.endInclusive.toInt()),
        dormSelection = selectedDorms,
        bathSelection = selectedBaths,
        seenOnly = seenOnly,
        notSeenOnly = notSeenOnly,
        seenAndNotSeen = seenAndNotSeen,
        longTermOnly = longTermOnly,
        showReserved = showReserved,
        showRented = showRented
    )

    fun fromFilter(searchOption: SearchOption) = FilterState(
        priceRange = searchOption.priceRange.first.toFloat()..searchOption.priceRange.second.toFloat(),
        surfaceRange = searchOption.surfaceRange.first.toFloat()..searchOption.surfaceRange.second.toFloat(),
        selectedBaths = searchOption.bathSelection,
        selectedDorms = searchOption.dormSelection,
        seenOnly = searchOption.seenOnly,
        notSeenOnly = searchOption.notSeenOnly,
        seenAndNotSeen = searchOption.seenAndNotSeen,
        longTermOnly = searchOption.longTermOnly,
        showRented = searchOption.showRented,
        showReserved = searchOption.showReserved
    )
}