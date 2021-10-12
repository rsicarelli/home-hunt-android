package com.rsicarelli.homehunt.ui.composition

import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.ui.navigation.bottomBarDestinations

val LocalScaffoldDelegate =
    compositionLocalOf<ScaffoldDelegate> { error("ScaffoldDelegate not found") }

@Composable
@OptIn(ExperimentalComposeUiApi::class)
internal fun ProvideHomeHuntCompositionLocals(
    content: @Composable () -> Unit
) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val showBottomBar = navBackStackEntry?.destination?.route in bottomBarDestinations
    val context = LocalContext.current

    val scaffoldDelegate by remember {
        mutableStateOf(
            ScaffoldDelegate(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                navController = navController,
                context = context,
                showStatusBar = showBottomBar
            )
        )
    }

    CompositionLocalProvider(LocalScaffoldDelegate provides scaffoldDelegate) {
        content()
    }

}