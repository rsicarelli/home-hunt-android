package com.rsicarelli.homehunt.presentation.filter

import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.domain.model.PropertyVisibility.NotSeen
import com.rsicarelli.homehunt.domain.model.PropertyVisibility.Seen
import com.rsicarelli.homehunt.domain.model.SearchOption

data class FilterState(
    val priceRange: ClosedFloatingPointRange<Float> = 0f..1600F,
    val surfaceRange: ClosedFloatingPointRange<Float> = 0f..300f,
    val dormCount: Int = 0,
    val bathCount: Int = 0,
    val seenOnly: Seen? = null,
    val notSeenOnly: NotSeen? = null,
    val longTermOnly: Boolean = false,
    val previewResultCount: Int? = null,
    val showReserved: Boolean = false,
    val showRented: Boolean = false,
    val uiEvent: UiEvent = UiEvent.Idle,
) {
    fun toFilter() = SearchOption(
        priceRange = Pair(priceRange.start.toDouble(), priceRange.endInclusive.toDouble()),
        surfaceRange = Pair(surfaceRange.start.toInt(), surfaceRange.endInclusive.toInt()),
        dormCount = dormCount,
        bathCount = bathCount,
        seenOnly = seenOnly,
        notSeenOnly = notSeenOnly,
        longTermOnly = longTermOnly,
        showReserved = showReserved,
        showRented = showRented
    )

    fun fromFilter(searchOption: SearchOption) = FilterState(
        priceRange = searchOption.priceRange.first.toFloat()..searchOption.priceRange.second.toFloat(),
        surfaceRange = searchOption.surfaceRange.first.toFloat()..searchOption.surfaceRange.second.toFloat(),
        bathCount = searchOption.bathCount,
        dormCount = searchOption.dormCount,
        seenOnly = searchOption.seenOnly,
        notSeenOnly = searchOption.notSeenOnly,
        longTermOnly = searchOption.longTermOnly,
        showRented = searchOption.showRented,
        showReserved = searchOption.showReserved
    )
}