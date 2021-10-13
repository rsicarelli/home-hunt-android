package com.rsicarelli.homehunt.presentation.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.presentation.components.FilterFab
import com.rsicarelli.homehunt.presentation.map.components.PropertiesMapView
import com.rsicarelli.homehunt.ui.navigation.Screen


@Composable
fun MapScreen(
    scaffoldDelegate: ScaffoldDelegate,
    viewModel: MapViewModel = hiltViewModel(),
) {
    MapContent(state = viewModel.state.value) {

    }
}

@Composable
private fun MapContent(
    state: MapState,
    onNavigate: (String) -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {

        if (state.properties.isNotEmpty()) {
            PropertiesMapView(onMarkerClick = {}, locations = state.properties)
        }

        FilterFab(false) {
            onNavigate(Screen.Filter.route)
        }
    }
}