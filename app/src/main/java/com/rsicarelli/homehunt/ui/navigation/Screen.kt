package com.rsicarelli.homehunt.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {
    object SplashScreen : Screen("splash_screen", arguments = emptyList())
    object LoginScreen : Screen("login_screen", arguments = emptyList())

    object HomeScreen : Screen(route = "home_screen")
    object Filter : Screen("filter_screen", emptyList())

    object PropertyDetail :
        Screen(
            "property_details_screen",
            arguments = listOf(navArgument("referenceId") {
                type = NavType.StringType
            })
        )
}
