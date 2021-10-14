package com.rsicarelli.homehunt.presentation.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rsicarelli.homehunt.core.model.HomeHuntState
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.core.model.UiText
import com.rsicarelli.homehunt.core.model.isLoading
import com.rsicarelli.homehunt.presentation.components.CircularIndeterminateProgressBar
import com.rsicarelli.homehunt.presentation.filter.FilterState
import com.rsicarelli.homehunt.presentation.login.components.GoogleSignInOption
import com.rsicarelli.homehunt.presentation.login.components.Welcome
import com.rsicarelli.homehunt.ui.theme.HomeHuntTheme
import com.rsicarelli.homehunt.ui.theme.Size_2X_Large

@Composable
fun LoginScreen(
    homeHuntState: HomeHuntState
) {
    val viewModel: LoginViewModel = hiltViewModel()

    val state by viewModel.state.collectAsState(LoginState())

    LoginContent(
        state = state,
        events = viewModel::onEvent,
        onShowMessageToUser = {
            homeHuntState.showMessageToUser(it)
        },
        onNavigateSingleTop = {
            homeHuntState.navigateSingleTop(it)
        }
    )
}

@Composable
private fun LoginContent(
    state: LoginState,
    events: (LoginEvents) -> Unit,
    onShowMessageToUser: (UiText) -> Unit,
    onNavigateSingleTop: (String) -> Unit,
) {
    when (state.uiEvent) {
        is UiEvent.MessageToUser -> onShowMessageToUser(state.uiEvent.uiText)
        is UiEvent.Navigate -> onNavigateSingleTop(state.uiEvent.route)
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Welcome()

        Spacer(modifier = Modifier.height(Size_2X_Large))

        if (!state.progressBarState.isLoading()) {
            GoogleSignInOption(events)
        } else {
            CircularIndeterminateProgressBar(state.progressBarState)
        }
    }
}

@Composable
@Preview
private fun LoginScreenPreview() {
    HomeHuntTheme(isPreview = true) {
        LoginContent(
            state = LoginState(),
            events = {},
            onShowMessageToUser = {},
            onNavigateSingleTop = {}
        )
    }
}
