package com.rsicarelli.homehunt.presentation.filter

sealed class FilterEvents {
    data class PriceRangeChanged(val range: ClosedFloatingPointRange<Float>) :
        FilterEvents()

    data class SurfaceRangeChanged(val range: ClosedFloatingPointRange<Float>) :
        FilterEvents()

    data class DormsSelectedChange(val newValue: Int) : FilterEvents()
    data class BathSelectedChange(val newValue: Int) : FilterEvents()
    object SaveFilter : FilterEvents()
    object ClearFilter : FilterEvents()
}