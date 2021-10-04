package com.rsicarelli.homehunt.presentation.login

import com.rsicarelli.homehunt.core.model.UiEvent

data class LoginState(
    val uiEvent: UiEvent = UiEvent.Idle
)