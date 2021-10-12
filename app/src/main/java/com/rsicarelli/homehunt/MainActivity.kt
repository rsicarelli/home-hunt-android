package com.rsicarelli.homehunt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import com.rsicarelli.homehunt.presentation.favourites.FavouritesScreen
import com.rsicarelli.homehunt.presentation.filter.FilterScreen
import com.rsicarelli.homehunt.presentation.home.HomeScreen
import com.rsicarelli.homehunt.presentation.login.LoginScreen
import com.rsicarelli.homehunt.presentation.propertyDetail.PropertyDetailScreen
import com.rsicarelli.homehunt.presentation.splash.SplashScreen
import com.rsicarelli.homehunt.ui.components.AppScaffold
import com.rsicarelli.homehunt.ui.composition.LocalScaffoldDelegate
import com.rsicarelli.homehunt.ui.composition.ProvideHomeHuntCompositionLocals
import com.rsicarelli.homehunt.ui.navigation.NavArguments
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.theme.HomeHuntTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            HomeHuntTheme {
                ProvideWindowInsets {
                    ProvideHomeHuntCompositionLocals {
                        MainContent()
                    }
                }
            }
        }
    }
}

@Composable
private fun MainContent(
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(top = false)
    ) {
        val scaffoldDelegate = LocalScaffoldDelegate.current

        AppScaffold {
            NavHost(
                navController = scaffoldDelegate.navController,
                startDestination = Screen.Home.route, //Test only
                modifier = Modifier.fillMaxSize()
            ) {
                composable(Screen.Splash.route) {
                    SplashScreen()
                }

                composable(Screen.Login.route) {
                    LoginScreen()
                }

                composable(Screen.Home.route) {
                    HomeScreen()
                }

                composable(
                    route = Screen.PropertyDetail.route + "/{${NavArguments.PROPERTY_DETAIL}}",
                    arguments = Screen.PropertyDetail.arguments
                ) {
                    PropertyDetailScreen()
                }

                composable(Screen.Filter.route) {
                    FilterScreen()
                }
                composable(Screen.Favourites.route) {
                    FavouritesScreen()
                }
            }
        }
    }
}

