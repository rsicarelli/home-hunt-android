package com.rsicarelli.homehunt.presentation.home

import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.domain.model.Property

sealed class HomeState() {
    object Loading : HomeState()
    object EmptyResults : HomeState()
    object Idle : HomeState()
    data class Error(val messageToUser: UiEvent.MessageToUser)
    data class Loaded(val properties: List<Property>) : HomeState()
}