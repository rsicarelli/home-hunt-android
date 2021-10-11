package com.rsicarelli.homehunt.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.ui.navigation.BottomNavItem
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.navigation.bottomBarDestinations
import com.rsicarelli.homehunt.ui.theme.Secondary
import com.rsicarelli.homehunt.ui.theme.SpaceSmall

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    navController: NavController,
    state: ScaffoldState,
    content: @Composable () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val showBottomBar = navBackStackEntry?.destination?.route in bottomBarDestinations

    SystemBarEffect()

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                HomeHuntBottomNavigation(navController)
            }
        },
        scaffoldState = state,
        modifier = modifier
    ) {
        content()
    }
}

@Composable
private fun SystemBarEffect() {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    val systemBarColor = MaterialTheme.colors.surface

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Secondary.copy(alpha = 0.4f),
            darkIcons = useDarkIcons
        )
        systemUiController.setNavigationBarColor(
            color = systemBarColor,
            darkIcons = useDarkIcons,
        )
    }
}

@Composable
fun HomeHuntBottomNavigation(navController: NavController) {
    val items: List<BottomNavItem> = listOf(
        BottomNavItem(
            route = Screen.Home.route,
            icon = Icons.Rounded.Home,
            contentDescription = stringResource(id = R.string.home)
        ),
        BottomNavItem(
            route = Screen.Favourites.route,
            icon = Icons.Rounded.Favorite,
            contentDescription = stringResource(id = R.string.favourites)
        )
    )

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.surface,
        contentColor = Color.White
    ) {
        items.forEach { item ->
            val selected = item.route == navController.currentDestination?.route

            val lineLength = animateFloatAsState(
                targetValue = if (selected) 1f else 0f,
                animationSpec = tween(
                    durationMillis = 300
                )
            )

            BottomNavigationItem(
                icon = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(SpaceSmall)
                            .drawBehind {
                                if (lineLength.value > 0f) {
                                    drawLine(
                                        color = if (selected) Color.White
                                        else Color.White.copy(0.4f),
                                        start = Offset(
                                            size.width / 2f - lineLength.value * 15.dp.toPx(),
                                            size.height
                                        ),
                                        end = Offset(
                                            size.width / 2f + lineLength.value * 15.dp.toPx(),
                                            size.height
                                        ),
                                        strokeWidth = 2.dp.toPx(),
                                        cap = StrokeCap.Round
                                    )
                                }


                            }
                    ) {
                        if (item.icon != null) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.contentDescription,
                                modifier = Modifier
                                    .align(Alignment.Center)
                            )
                        }
                    }
                },
                selected = selected,
                onClick = {
                    if (navController.currentDestination?.route != item.route) {
                        navController.navigate(item.route)
                    }
                }
            )
        }
    }
}
