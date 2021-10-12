package com.rsicarelli.homehunt.core.model

import android.content.Context
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.rsicarelli.homehunt.core.util.asString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//TODO Improve this class which look like an "utils" one :face_palm:
class ScaffoldDelegate(
    val coroutineScope: CoroutineScope,
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    val context: Context,
    val showStatusBar: Boolean
) {

    fun showMessageToUser(uiText: UiText) {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = uiText.asString(context),
                duration = SnackbarDuration.Short
            )
        }
    }

    fun navigateSingleTop(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    fun navigate(uiEvent: UiEvent.Navigate) {
        navController.navigate(uiEvent.route)
    }

    fun navigate(route: String) {
        navController.navigate(route)
    }

    fun navigateUp() {
        navController.navigateUp()
    }
}