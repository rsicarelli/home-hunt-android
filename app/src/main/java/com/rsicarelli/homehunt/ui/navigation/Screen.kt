package com.rsicarelli.homehunt.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val arguments: List<NamedNavArgument>) {
    object SplashScreen : Screen("splash_screen", arguments = emptyList())
    object LoginScreen : Screen("login_screen", arguments = emptyList())

    object HomeScreen : Screen(
        route = "home_screen",
        arguments = listOf(navArgument("reference") {
            type = NavType.StringType
        })
    )

    object Filter : Screen("filter_screen", emptyList())
}
