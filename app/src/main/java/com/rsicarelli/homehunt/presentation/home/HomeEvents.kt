package com.rsicarelli.homehunt.presentation.home

import androidx.lifecycle.Lifecycle

sealed class HomeEvents {
    object GetProperties : HomeEvents()
    data class LifecycleEvent(val event: Lifecycle.Event) : HomeEvents()
    data class ToggleFavourite(val reference: String, val isFavourited: Boolean) : HomeEvents()
}