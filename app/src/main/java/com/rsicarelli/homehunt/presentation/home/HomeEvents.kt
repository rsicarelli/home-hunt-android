package com.rsicarelli.homehunt.presentation.home

import androidx.lifecycle.Lifecycle

sealed class HomeEvents {
    object GetProperties : HomeEvents()
    data class ToggleFavourite(val reference: String, val isFavourited: Boolean) : HomeEvents()
}