package com.rsicarelli.homehunt.presentation.filter

import androidx.lifecycle.Lifecycle

sealed class FilterEvents {
    data class PriceRangeChanged(val range: ClosedFloatingPointRange<Float>) :
        FilterEvents()

    data class SurfaceRangeChanged(val range: ClosedFloatingPointRange<Float>) :
        FilterEvents()

    data class DormsSelectedChange(val newValue: Int) : FilterEvents()
    data class BathSelectedChange(val newValue: Int) : FilterEvents()
    data class LifecycleEvent(val event: Lifecycle.Event) : FilterEvents()

    object SaveFilter : FilterEvents()
    object ClearFilter : FilterEvents()
}