package com.rsicarelli.homehunt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.presentation.components.AppScaffold
import com.rsicarelli.homehunt.presentation.favourites.FavouritesScreen
import com.rsicarelli.homehunt.presentation.filter.FilterScreen
import com.rsicarelli.homehunt.presentation.filter.FilterViewModel
import com.rsicarelli.homehunt.presentation.home.HomeScreen
import com.rsicarelli.homehunt.presentation.home.HomeViewModel
import com.rsicarelli.homehunt.presentation.login.LoginScreen
import com.rsicarelli.homehunt.presentation.login.LoginViewModel
import com.rsicarelli.homehunt.presentation.propertyDetail.PropertyDetailScreen
import com.rsicarelli.homehunt.presentation.splash.SplashScreen
import com.rsicarelli.homehunt.presentation.splash.SplashViewModel
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.theme.HomeHuntTheme
import com.rsicarelli.homehunt.ui.theme.Secondary
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Turn off the decor fitting system windows, which means we need to through handling
        // insets
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            HomeHuntTheme {
                ProvideWindowInsets {
                    Surface(
                        color = MaterialTheme.colors.background,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val navController = rememberNavController()
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val scaffoldState = rememberScaffoldState()
                        val context = LocalContext.current
                        val coroutineScope = rememberCoroutineScope()
                        val scaffoldDelegate by remember {
                            mutableStateOf(
                                ScaffoldDelegate(
                                    coroutineScope,
                                    scaffoldState,
                                    navController,
                                    context
                                )
                            )
                        }
                        AppScaffold(
                            navController = navController,
                            showBottomBar = navBackStackEntry?.destination?.route in listOf(
                                Screen.Home.route,
                                Screen.Favourites.route
                            ),
                            state = scaffoldState,
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = Screen.Splash.route, //Test only
                                modifier = Modifier.fillMaxSize()
                            ) {
                                composable(Screen.Login.route) {
                                    LoginScreen(
                                        scaffoldDelegate = scaffoldDelegate,
                                    )
                                }

                                composable(
                                    Screen.PropertyDetail.route + "/{referenceId}",
                                    arguments = Screen.PropertyDetail.arguments
                                ) {
                                    PropertyDetailScreen(
                                        imageLoader,
                                        scaffoldDelegate
                                    )
                                }

                                composable(Screen.Home.route) {
                                    HomeScreen(
                                        scaffoldDelegate = scaffoldDelegate,
                                        imageLoader = imageLoader
                                    )
                                }

                                composable(Screen.Splash.route) {
                                    SplashScreen(
                                        scaffoldDelegate = scaffoldDelegate,
                                    )
                                }

                                composable(Screen.Filter.route) {
                                    FilterScreen(
                                        scaffoldDelegate = scaffoldDelegate,
                                    )
                                }
                                composable(Screen.Favourites.route) {
                                    FavouritesScreen(
                                        scaffoldDelegate = scaffoldDelegate,
                                        imageLoader = imageLoader
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

