package com.rsicarelli.homehunt.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {
    object Splash : Screen("splash", arguments = emptyList())
    object Login : Screen("login", arguments = emptyList())

    object Home : Screen(route = "home")
    object Favourites : Screen(route = "favourites")
    object Filter : Screen("filter", emptyList())

    object PropertyDetail :
        Screen(
            "property_details",
            arguments = listOf(navArgument("referenceId") {
                type = NavType.StringType
            })
        )
}
