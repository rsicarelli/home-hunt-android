package com.rsicarelli.homehunt.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.HomeHuntState
import com.rsicarelli.homehunt.presentation.components.CircularIndeterminateProgressBar
import com.rsicarelli.homehunt.presentation.components.EmptyContent
import com.rsicarelli.homehunt.presentation.components.FilterFab
import com.rsicarelli.homehunt.presentation.home.components.PropertyList
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.theme.HomeHuntTheme
import utils.Fixtures

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    homeHuntState: HomeHuntState,
) {
    val viewModel: HomeViewModel = hiltViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current

    val stateFlowLifecycleAware = remember(viewModel, lifecycleOwner) {
        viewModel.onEvent(HomeEvents.GetProperties)
        viewModel.state.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val state by stateFlowLifecycleAware.collectAsState(HomeState())

    HomeContent(
        events = viewModel::onEvent,
        state = state,
        onNavigate = { route ->
            homeHuntState.navigate(route)
        }
    )
}

@Composable
private fun HomeContent(
    events: (HomeEvents) -> Unit,
    state: HomeState,
    onNavigate: (String) -> Unit,
) {
    val scrollState = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        EmptyContent(state.emptyResults)

        PropertyList(
            scrollState = scrollState,
            properties = state.properties,
            headerPrefixRes = R.string.results,
            onNavigate = onNavigate,
            onToggleFavourite = { property ->
                events(
                    HomeEvents.ToggleFavourite(
                        property.reference,
                        !property.isFavourited
                    )
                )
            }
        )

        FilterFab(isScrollInProgress = scrollState.isScrollInProgress) {
            onNavigate(Screen.Filter.route)
        }

        CircularIndeterminateProgressBar(state.progressBarState)
    }
}

@Composable
@Preview
private fun HomeScreenPreview() {
    HomeHuntTheme(isPreview = true) {
        HomeContent(
            events = {},
            state = HomeState(properties = Fixtures.aListOfProperty, emptyResults = false),
            onNavigate = {}
        )
    }
}


