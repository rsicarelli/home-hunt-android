package com.rsicarelli.homehunt.presentation.login.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.util.extractAuthCredentials
import com.rsicarelli.homehunt.core.util.getGoogleSignInOptions
import com.rsicarelli.homehunt.presentation.login.LoginEvents
import com.rsicarelli.homehunt.ui.theme.BorderSizeSmallest
import com.rsicarelli.homehunt.ui.theme.Size_2X_Large
import com.rsicarelli.homehunt.ui.theme.Size_Large

@Composable
fun GoogleSignInOption(events: (LoginEvents) -> Unit) {
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            activityResult.extractAuthCredentials(
                onSuccess = { authCredential -> events(LoginEvents.Login(authCredential)) },
                onError = { exception -> events(LoginEvents.Error(exception)) })
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