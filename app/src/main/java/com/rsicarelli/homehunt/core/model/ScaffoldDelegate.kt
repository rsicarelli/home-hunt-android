package com.rsicarelli.homehunt.core.model

import android.content.Context
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rsicarelli.homehunt.core.util.asString
import com.rsicarelli.homehunt.ui.navigation.bottomBarDestinations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberScaffoldDelegate(): ScaffoldDelegate {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val scaffoldDelegate by remember {
        mutableStateOf(
            ScaffoldDelegate(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                navController = navController,
                context = context,
            )
        )
    }

    return scaffoldDelegate
}

//TODO find a better approach, this class looks a "utils" one :face_palm:
class ScaffoldDelegate(
    val coroutineScope: CoroutineScope,
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    val context: Context,
) {

    @Composable
    fun showBottomBar(): Boolean {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        return navBackStackEntry?.destination?.route in bottomBarDestinations
    }

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