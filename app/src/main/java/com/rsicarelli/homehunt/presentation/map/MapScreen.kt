package com.rsicarelli.homehunt.presentation.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.rsicarelli.homehunt.core.model.HomeHuntState
import com.rsicarelli.homehunt.presentation.components.FilterFab
import com.rsicarelli.homehunt.presentation.components.rememberOnLifecycle
import com.rsicarelli.homehunt.presentation.home.components.PropertyListItem
import com.rsicarelli.homehunt.presentation.map.components.PropertiesMapView
import com.rsicarelli.homehunt.ui.navigation.Screen
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
        onMarkerSelected = viewModel::onMarkerSelected,
        onMapClick = viewModel::onMapClicked
    )

    MapContent(
        state = state,
        actions = actions
    )
}

@OptIn(ExperimentalAnimationApi::class)
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
                locations = state.properties,
                onMapClick = actions.onMapClick
            )
        }

        AnimatedVisibility(
            visible = state.propertySnippet != null,
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            state.propertySnippet?.let { property ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            end = Size_Regular,
                            start = Size_Regular,
                            bottom = Size_Regular
                        )
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
        }

        FilterFab(false) {
            actions.onNavigate(Screen.Filter.route)
        }
    }
}