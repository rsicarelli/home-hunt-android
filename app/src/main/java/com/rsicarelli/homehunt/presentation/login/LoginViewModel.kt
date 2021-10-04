package com.rsicarelli.homehunt.presentation.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.core.model.UiEvent.*
import com.rsicarelli.homehunt.core.model.UiText
import com.rsicarelli.homehunt.domain.usecase.SignInUseCase
import com.rsicarelli.homehunt.domain.usecase.SignInUseCase.Request
import com.rsicarelli.homehunt.presentation.login.LoginEvents.Login
import com.rsicarelli.homehunt.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signIn: SignInUseCase
) : ViewModel() {

    private val _state: MutableState<LoginState> = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    fun onEvent(events: LoginEvents) {
        when (events) {
            is Login -> doLogin(events)
            is LoginEvents.Error -> state.showError(MessageToUser(UiText.DynamicString("Some error")))
        }
    }

    private fun doLogin(events: Login) {
        viewModelScope.launch {
            signIn(Request(events.credential)).collectLatest { dataState ->
                when (dataState) {
                    is DataState.Loading -> state.toggleLoading(dataState.progressBarState)
                    is DataState.Data -> {
                        state.navigate(Navigate(Screen.HomeScreen.route))
                    }
                    is DataState.Error -> state.showError(dataState.messageToUser)
                }
            }
        }
    }

    private fun State<LoginState>.toggleLoading(progressBarState: ProgressBarState) {
        _state.value = this.value.copy(uiEvent = Loading(progressBarState))
    }

    private fun State<LoginState>.navigate(navigate: Navigate) {
        _state.value = this.value.copy(uiEvent = Navigate(navigate.route))
    }

    private fun State<LoginState>.showError(messageToUiEvent: MessageToUser) {
        _state.value = this.value.copy(uiEvent = messageToUiEvent)
    }
}
