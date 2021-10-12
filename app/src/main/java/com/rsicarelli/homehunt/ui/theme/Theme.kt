package com.rsicarelli.homehunt.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.ui.components.AppScaffold
import com.rsicarelli.homehunt.ui.composition.ProvideHomeHuntCompositionLocals
import com.rsicarelli.homehunt.ui.navigation.bottomBarDestinations

private val DarkColorPalette = darkColors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    onPrimary = OnSecondary,
    secondary = Secondary,
    secondaryVariant = SecondaryVariant,
    onSecondary = OnSecondary,
    error = Error,
    onError = OnError,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
)


@Composable
fun HomeHuntTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isPreview: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = DarkColorPalette

    MaterialTheme(
        colors = colors,
        typography = EczarTypography,
        shapes = AppShapes,
        content = {
            if (isPreview) {
                Surface { content() }
            } else {
                content()
            }
        }
    )
}
