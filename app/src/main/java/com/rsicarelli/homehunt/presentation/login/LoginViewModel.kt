package com.rsicarelli.homehunt.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.core.model.UiEvent.MessageToUser
import com.rsicarelli.homehunt.core.model.UiEvent.Navigate
import com.rsicarelli.homehunt.core.model.UiText
import com.rsicarelli.homehunt.domain.usecase.SignInUseCase
import com.rsicarelli.homehunt.domain.usecase.SignInUseCase.Request
import com.rsicarelli.homehunt.presentation.login.LoginEvents.Login
import com.rsicarelli.homehunt.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signIn: SignInUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun onEvent(events: LoginEvents) {
        when (events) {
            is Login -> doLogin(events)
            is LoginEvents.Error -> showError()
        }
    }

    private fun doLogin(events: Login) {
        viewModelScope.launch {
            signIn(Request(events.credential))
                .onStart { toggleLoading(ProgressBarState.Loading) }
                .onCompletion { toggleLoading(ProgressBarState.Idle) }
                .catch { showError() }
                .collectLatest { outcome ->
                    when (outcome) {
                        SignInUseCase.Outcome.Error -> showError()
                        SignInUseCase.Outcome.Success -> navigate(Navigate(Screen.Home.route))
                    }
                }
        }
    }

    private fun toggleLoading(progressBarState: ProgressBarState) {
        _state.value = state.value.copy(progressBarState = progressBarState)
    }

    private fun navigate(navigate: Navigate) {
        _state.value = state.value.copy(uiEvent = Navigate(navigate.route))
    }

    private fun showError(messageToUiEvent: MessageToUser = MessageToUser(UiText.unknownError())) {
        _state.value = state.value.copy(uiEvent = messageToUiEvent)
    }
}
