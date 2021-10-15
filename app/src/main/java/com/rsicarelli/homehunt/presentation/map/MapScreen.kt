package com.rsicarelli.homehunt.presentation.map

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.insets.navigationBarsPadding
import com.rsicarelli.homehunt.core.model.HomeHuntState
import com.rsicarelli.homehunt.presentation.components.FilterFab
import com.rsicarelli.homehunt.presentation.components.rememberOnLifecycle
import com.rsicarelli.homehunt.presentation.home.components.PropertyListItem
import com.rsicarelli.homehunt.presentation.map.components.PropertiesMapView
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.theme.BottomBarSize
import com.rsicarelli.homehunt.ui.theme.Size_Regular


@Composable
fun MapScreen(
    homeHuntState: HomeHuntState,
    viewModel: MapViewModel = hiltViewModel(),
) {
    val stateFlowLifecycleAware = viewModel.rememberOnLifecycle {
        viewModel.init().flowWithLifecycle(
            lifecycle = it.lifecycle,
            minActiveState = Lifecycle.State.RESUMED
        )
    }

    val state by stateFlowLifecycleAware.collectAsState(initial = MapState())

    val actions = MapActions(
        onNavigate = homeHuntState::navigate,
        onToggleFavourite = viewModel::toggleFavourite,
        onPropertyViewed = viewModel::onPropertyViewed,
        onMarkerSelected = viewModel::onMarkerSelected
    )

    MapContent(
        state = state,
        actions = actions
    )
}

@Composable
private fun MapContent(
    state: MapState,
    actions: MapActions,
) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {

        if (state.properties.isNotEmpty()) {
            PropertiesMapView(
                onMarkerClick = actions.onMarkerSelected,
                locations = state.properties
            )
        }

        state.propertySnippet?.let { property ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Size_Regular)
            ) {
                item {
                    PropertyListItem(
                        property = property,
                        onSelectProperty = { actions.onNavigate("${Screen.PropertyDetail.route}/${property.reference}") },
                        onFavouriteClick = {
                            actions.onToggleFavourite(
                                property.reference,
                                !property.isFavourited
                            )
                        },
                        onViewedGallery = { actions.onPropertyViewed(property) },
                        gallerySize = 160.dp
                    )
                }

            }

        }

        FilterFab(false) {
            actions.onNavigate(Screen.Filter.route)
        }
    }
}