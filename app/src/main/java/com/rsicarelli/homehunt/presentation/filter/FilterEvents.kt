package com.rsicarelli.homehunt.presentation.filter

import androidx.lifecycle.Lifecycle

sealed class FilterEvents {
    data class PriceRangeChanged(val range: ClosedFloatingPointRange<Float>) :
        FilterEvents()

    data class SurfaceRangeChanged(val range: ClosedFloatingPointRange<Float>) :
        FilterEvents()

    data class DormsSelectionChanged(val newValue: Int) : FilterEvents()
    data class BathSelectionChanged(val newValue: Int) : FilterEvents()
    data class VisibilitySelectionChanged(val newValue: Boolean) : FilterEvents()
    data class LongerTermRentalSelectionChanged(val newValue: Boolean) : FilterEvents()
    data class AvailabilitySelectionChanged(val newValue: Boolean) : FilterEvents()

    object SaveFilter : FilterEvents()
    object ClearFilter : FilterEvents()
    object GetFilter : FilterEvents()
}