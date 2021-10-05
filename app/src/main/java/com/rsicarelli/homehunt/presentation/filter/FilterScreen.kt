package com.rsicarelli.homehunt.presentation.filter

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate

@Composable
fun FilterScreen(
    scaffoldDelegate: ScaffoldDelegate,
    state: FilterState,
    events: (FilterEvents) -> Unit
) {
    Text("I'm a filter page!")
}