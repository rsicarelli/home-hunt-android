package com.rsicarelli.homehunt.core.model

import android.content.Context
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.navigation.NavController
import com.rsicarelli.homehunt.core.util.asString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ScaffoldDelegate(
    private val coroutineScope: CoroutineScope,
    private val scaffoldState: ScaffoldState,
    private val navController: NavController,
    private val context: Context
) {

    fun showMessageToUser(uiText: UiText) {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = uiText.asString(context),
                duration = SnackbarDuration.Short
            )
        }
    }

    fun navigateSingleTop(uiEvent: UiEvent.Navigate) {
        navController.navigate(uiEvent.route) {
            launchSingleTop = true
        }
    }

    fun navigateUp() {
        navController.navigateUp()
    }
}