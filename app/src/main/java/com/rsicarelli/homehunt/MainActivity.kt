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
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.rsicarelli.homehunt.presentation.components.AppScaffold
import com.rsicarelli.homehunt.presentation.home.HomeScreen
import com.rsicarelli.homehunt.presentation.home.HomeViewModel
import com.rsicarelli.homehunt.presentation.login.LoginScreen
import com.rsicarelli.homehunt.presentation.login.LoginViewModel
import com.rsicarelli.homehunt.presentation.splash.SplashScreen
import com.rsicarelli.homehunt.presentation.splash.SplashViewModel
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.theme.HomeHuntTheme
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
        setContent {
            HomeHuntTheme {
                BoxWithConstraints {
                    Surface(
                        color = MaterialTheme.colors.background,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val navController = rememberNavController()
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val scaffoldState = rememberScaffoldState()
                        AppScaffold(
                            navController = navController,
                            showBottomBar = navBackStackEntry?.destination?.route in listOf(
                                Screen.HomeScreen.route,
                            ),
                            state = scaffoldState,
                            modifier = Modifier.fillMaxSize(),
                        ) { scaffoldDelegate ->
                            NavHost(
                                navController = navController,
                                startDestination = Screen.SplashScreen.route,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                composable(Screen.LoginScreen.route) {
                                    val viewModel: LoginViewModel = hiltViewModel()
                                    LoginScreen(
                                        scaffoldDelegate = scaffoldDelegate,
                                        state = viewModel.state.value,
                                        events = viewModel::onEvent
                                    )
                                }

                                composable(Screen.HomeScreen.route) {
                                    val viewModel: HomeViewModel = hiltViewModel()
                                    HomeScreen(
                                        scaffoldDelegate = scaffoldDelegate,
                                        state = viewModel.state.value,
                                        events = viewModel::onEvent,
                                        imageLoader = imageLoader
                                    )
                                }

                                composable(Screen.SplashScreen.route) {
                                    val viewModel: SplashViewModel = hiltViewModel()
                                    SplashScreen(
                                        scaffoldDelegate = scaffoldDelegate,
                                        state = viewModel.state.value,
                                        events = viewModel::onEvent
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

