package com.rsicarelli.homehunt.core.model

sealed class UiEvent {
    data class MessageToUser(val uiText: UiText) : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class Loading(val progressBarState: ProgressBarState = ProgressBarState.Idle) : UiEvent()
    object NavigateUp : UiEvent()
    object Idle : UiEvent()
}