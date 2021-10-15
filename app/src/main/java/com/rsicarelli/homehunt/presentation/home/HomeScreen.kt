package com.rsicarelli.homehunt.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.HomeHuntState
import com.rsicarelli.homehunt.presentation.components.CircularIndeterminateProgressBar
import com.rsicarelli.homehunt.presentation.components.EmptyContent
import com.rsicarelli.homehunt.presentation.components.FilterFab
import com.rsicarelli.homehunt.presentation.components.rememberOnLifecycle
import com.rsicarelli.homehunt.presentation.home.components.PropertyList
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.theme.HomeHuntTheme
import utils.Fixtures

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    homeHuntState: HomeHuntState,
    viewModel: HomeViewModel = hiltViewModel()

) {
    val stateFlowLifecycleAware = viewModel.rememberOnLifecycle {
        viewModel.init().flowWithLifecycle(
            lifecycle = it.lifecycle,
            minActiveState = Lifecycle.State.STARTED
        )
    }

    val state by stateFlowLifecycleAware.collectAsState(initial = HomeState())

    val actions = HomeActions(
        onNavigate = homeHuntState::navigate,
        onToggleFavourite = viewModel::toggleFavourite
    )

    HomeContent(
        state = state,
        actions = actions,
    )
}

@Composable
private fun HomeContent(
    state: HomeState,
    actions: HomeActions,
) {
    val scrollState = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        state.properties.takeIf { it.isNotEmpty() }
            ?.let {
                PropertyList(
                    scrollState = scrollState,
                    properties = it,
                    headerPrefixRes = R.string.results,
                    onNavigate = actions.onNavigate,
                    onToggleFavourite = actions.onToggleFavourite
                )
            } ?: EmptyContent()

        FilterFab(isScrollInProgress = scrollState.isScrollInProgress) {
            actions.onNavigate(Screen.Filter.route)
        }

        CircularIndeterminateProgressBar(state.progressBarState)
    }
}

@Composable
@Preview
private fun HomeScreenPreview() {
    HomeHuntTheme(isPreview = true) {
        HomeContent(
            actions = HomeActions({ _, _ -> }, { }),
            state = HomeState(properties = Fixtures.aListOfProperty),
        )
    }
}


