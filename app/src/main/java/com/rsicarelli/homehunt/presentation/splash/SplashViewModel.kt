package com.rsicarelli.homehunt.presentation.splash

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.domain.usecase.IsLoggedInUseCase
import com.rsicarelli.homehunt.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val isLoggedIn: IsLoggedInUseCase
) : ViewModel() {

    private val _state: MutableState<SplashState> = mutableStateOf(SplashState())
    val state: State<SplashState> = _state

    fun onEvent(events: SplashEvents) {
        when (events) {
            SplashEvents.AnimationEnded -> {
                routeUser()
            }
        }
    }

    private fun routeUser() {
        viewModelScope.launch {
            isLoggedIn().collect { dataState ->
                val isLoggedIn = dataState.data!!
                if (isLoggedIn) navigateToHome() else navigateToLogin()
            }
        }
    }

    private fun navigateToHome() {
        _state.value = state.value.copy(uiEvent = UiEvent.Navigate(Screen.Home.route))
    }

    private fun navigateToLogin() {
        _state.value = state.value.copy(uiEvent = UiEvent.Navigate(Screen.Login.route))
    }
}