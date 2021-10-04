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
import androidx.compose.ui.unit.dp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.core.util.extractAuthCredentials
import com.rsicarelli.homehunt.presentation.login.LoginEvents.Error
import com.rsicarelli.homehunt.presentation.login.LoginEvents.Login
import com.rsicarelli.homehunt.ui.theme.SpaceLarge

@Composable
fun LoginScreen(
    scaffoldDelegate: ScaffoldDelegate,
    state: LoginState,
    events: (LoginEvents) -> Unit
) {
    scaffoldDelegate.handleUiState(state.uiEvent)

    val context = LocalContext.current
    val token = stringResource(R.string.default_web_client_id)

    val launcher = rememberLauncherForActivityResult(StartActivityForResult()) { activityResult ->
        activityResult.extractAuthCredentials(
            onSuccess = { authCredential -> events(Login(authCredential)) },
            onError = { exception -> events(Error(exception)) })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = SpaceLarge,
                end = SpaceLarge,
                top = SpaceLarge,
                bottom = 50.dp
            )
    ) {
        OutlinedButton(
            border = ButtonDefaults.outlinedBorder.copy(width = 1.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(token)
                    .requestEmail()
                    .build()

                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                launcher.launch(googleSignInClient.signInIntent)
            },
            content = {
                if (state.uiEvent is UiEvent.Loading) {
                    return@OutlinedButton
                }

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
                            text = "Google"
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