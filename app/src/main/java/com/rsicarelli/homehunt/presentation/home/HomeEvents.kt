package com.rsicarelli.homehunt.presentation.home

import androidx.lifecycle.Lifecycle

sealed class HomeEvents {
    data class LifecycleEvent(val event: Lifecycle.Event) : HomeEvents()
}