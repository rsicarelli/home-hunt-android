package com.rsicarelli.homehunt.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

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
fun HomeHuntTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = DarkColorPalette

    MaterialTheme(
        colors = colors,
        typography = EczarTypography,
        shapes = AppShapes,
        content = content
    )
}