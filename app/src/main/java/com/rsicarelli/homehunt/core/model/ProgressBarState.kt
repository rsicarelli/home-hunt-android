package com.rsicarelli.homehunt.core.model

sealed class ProgressBarState {
    object Loading : ProgressBarState()
    object Idle : ProgressBarState()
}