package com.rsicarelli.homehunt.presentation.filter

sealed class FilterEvents {
    object SaveFilter : FilterEvents()
    object ClearFilter : FilterEvents()
}