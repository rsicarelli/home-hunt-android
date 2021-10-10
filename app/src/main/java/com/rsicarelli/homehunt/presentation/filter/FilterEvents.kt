package com.rsicarelli.homehunt.presentation.filter

import androidx.lifecycle.Lifecycle
import com.rsicarelli.homehunt.domain.model.PropertyVisibility

sealed class FilterEvents {
    data class PriceRangeChanged(val range: ClosedFloatingPointRange<Float>) :
        FilterEvents()

    data class SurfaceRangeChanged(val range: ClosedFloatingPointRange<Float>) :
        FilterEvents()

    data class DormsSelectionChanged(val newValue: Int) : FilterEvents()
    data class BathSelectionChanged(val newValue: Int) : FilterEvents()
    data class VisibilitySelectionChanged(val newValue: PropertyVisibility) : FilterEvents()
    data class LifecycleEvent(val event: Lifecycle.Event) : FilterEvents()

    object SaveFilter : FilterEvents()
    object ClearFilter : FilterEvents()
}