package com.rsicarelli.homehunt.presentation.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.core.model.isLoading
import com.rsicarelli.homehunt.core.util.extractAuthCredentials
import com.rsicarelli.homehunt.core.util.getGoogleSignInOptions
import com.rsicarelli.homehunt.presentation.components.CircularIndeterminateProgressBar
import com.rsicarelli.homehunt.presentation.login.LoginEvents.Error
import com.rsicarelli.homehunt.presentation.login.LoginEvents.Login
import com.rsicarelli.homehunt.ui.composition.LocalScaffoldDelegate
import com.rsicarelli.homehunt.ui.theme.BorderSizeSmallest
import com.rsicarelli.homehunt.ui.theme.Size_Regular
import com.rsicarelli.homehunt.ui.theme.Size_Large
import com.rsicarelli.homehunt.ui.theme.Size_2X_Large

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val scaffoldDelegate = LocalScaffoldDelegate.current

    HomeContent(viewModel.state.value, scaffoldDelegate, viewModel::onEvent)
}

@Composable
private fun HomeContent(
    state: LoginState,
    scaffoldDelegate: ScaffoldDelegate,
    events: (LoginEvents) -> Unit
) {
    when (state.uiEvent) {
        is UiEvent.MessageToUser -> scaffoldDelegate.showMessageToUser(state.uiEvent.uiText)
        is UiEvent.Navigate -> scaffoldDelegate.navigateSingleTop(state.uiEvent.route)
        UiEvent.NavigateUp -> scaffoldDelegate.navigateUp()
    }

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Welcome()
        BrandingSentence()

        Spacer(modifier = Modifier.height(Size_Large))
        Spacer(modifier = Modifier.height(Size_Large))

        if (!state.progressBarState.isLoading()) {
            GoogleSignInOption(events)
        } else {
            CircularIndeterminateProgressBar(state.progressBarState)
        }
    }
}

@Composable
private fun Welcome() {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Size_2X_Large, end = Size_Large, bottom = Size_Large, start = Size_Large),
        text = stringResource(id = R.string.welcome),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h4,
        color = Color.White
    )
}

@Composable
private fun BrandingSentence() {
    Text(
        text = stringResource(id = R.string.do_login),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(Size_Regular),
        color = Color.White.copy(alpha = 0.5f)
    )
}

@Composable
private fun GoogleSignInOption(events: (LoginEvents) -> Unit) {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(StartActivityForResult()) { activityResult ->
        activityResult.extractAuthCredentials(
            onSuccess = { authCredential -> events(Login(authCredential)) },
            onError = { exception -> events(Error(exception)) })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = Size_Large,
                end = Size_Large,
                top = Size_Large,
                bottom = Size_2X_Large
            )
    ) {
        OutlinedButton(
            border = ButtonDefaults.outlinedBorder.copy(width = BorderSizeSmallest),
            modifier = Modifier
                .fillMaxWidth()
                .height(Size_2X_Large),
            onClick = {
                launcher.launch(context.getGoogleSignInOptions().signInIntent)
            },
            content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    content = {
                        Icon(
                            tint = Color.Unspecified,
                            painter = painterResource(id = R.drawable.googleg_standard_color_18),
                            contentDescription = null,
                        )
                        Text(
                            style = MaterialTheme.typography.button,
                            color = MaterialTheme.colors.onSurface,
                            text = stringResource(id = R.string.google)
                        )
                        Icon(
                            tint = Color.Transparent,
                            imageVector = Icons.Default.MailOutline,
                            contentDescription = null,
                        )
                    }
                )
            }
        )
    }


}