package com.rsicarelli.homehunt.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.ui.navigation.BottomNavItem
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.theme.HintGray
import com.rsicarelli.homehunt.ui.theme.Secondary
import com.rsicarelli.homehunt.ui.theme.SpaceMedium
import com.rsicarelli.homehunt.ui.theme.SpaceSmall

@Composable
fun AppScaffold(
    modifier: Modifier = Modifier,
    navController: NavController,
    showBottomBar: Boolean = true,
    state: ScaffoldState,
    bottomNavItems: List<BottomNavItem> = listOf(
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
    ),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = MaterialTheme.colors.isLight
    SideEffect {
        systemUiController.setSystemBarsColor(
            Secondary.copy(alpha = 0.4f),
            darkIcons = useDarkIcons
        )
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                TestNavigation(navController)
            }
        },
        scaffoldState = state,
        modifier = modifier
    ) {
        content()
    }
}

@Composable
private fun AppBottomNavigation(
    bottomNavItems: List<BottomNavItem>,
    navController: NavController
) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding()
    ) {
        BottomNavigation {
            bottomNavItems.forEachIndexed { _, bottomNavItem ->
                AppBottomNavItem(
                    icon = bottomNavItem.icon,
                    iconPainter = bottomNavItem.painter,
                    contentDescription = bottomNavItem.contentDescription,
                    selected = bottomNavItem.route == navController.currentDestination?.route,
                    enabled = bottomNavItem.icon != null
                ) {
                    if (navController.currentDestination?.route != bottomNavItem.route) {
                        navController.navigate(bottomNavItem.route)
                    }
                }
            }
        }
    }
}

@Composable
@Throws(IllegalArgumentException::class)
fun RowScope.AppBottomNavItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconPainter: Painter? = null,
    contentDescription: String? = null,
    selected: Boolean = false,
    selectedColor: Color = MaterialTheme.colors.primary,
    unselectedColor: Color = HintGray,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val lineLength = animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(
            durationMillis = 300
        )
    )

    BottomNavigationItem(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        selectedContentColor = selectedColor,
        unselectedContentColor = unselectedColor,
        icon = {

        }
    )
}

@Composable
fun TestNavigation(navController: NavController) {
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
