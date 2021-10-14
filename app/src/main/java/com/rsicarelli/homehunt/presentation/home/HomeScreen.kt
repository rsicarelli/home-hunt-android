package com.rsicarelli.homehunt.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
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
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.presentation.components.CircularIndeterminateProgressBar
import com.rsicarelli.homehunt.presentation.components.EmptyContent
import com.rsicarelli.homehunt.presentation.components.FilterFab
import com.rsicarelli.homehunt.presentation.home.components.PropertyList
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.theme.HomeHuntTheme
import kotlinx.coroutines.flow.Flow
import utils.Fixtures

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    homeHuntState: HomeHuntState,
) {
    val viewModel: HomeViewModel = hiltViewModel()

    val lifecycleOwner = LocalLifecycleOwner.current
    val stateFlowLifecycleAware: Flow<HomeState> = remember(viewModel, lifecycleOwner) {
        viewModel.getProperties()
            .flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
    }

    val state by stateFlowLifecycleAware.collectAsState(initial = HomeState.Idle)

    val actions = HomeActions(
        onToggleFavourite = { referenceId, isFavourited ->
            viewModel.toggleFavourite(
                referenceId = referenceId,
                isFavourited = isFavourited
            )
        },
        onNavigate = { route -> homeHuntState.navigate(route) }
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
        when (state) {
            HomeState.EmptyResults -> EmptyContent(true)
            is HomeState.Loaded -> {
                PropertyList(
                    scrollState = scrollState,
                    properties = state.properties,
                    headerPrefixRes = R.string.results,
                    onNavigate = actions.onNavigate,
                    onToggleFavourite = actions.onToggleFavourite
                )
            }
            HomeState.Loading -> CircularIndeterminateProgressBar()
            HomeState.Idle -> Unit
        }

        FilterFab(isScrollInProgress = scrollState.isScrollInProgress) {
            actions.onNavigate(Screen.Filter.route)
        }
    }
}

@Composable
@Preview
private fun HomeScreenPreview() {
    HomeHuntTheme(isPreview = true) {
        HomeContent(
            actions = HomeActions({ _, _ -> }, { }),
            state = HomeState.Loaded(properties = Fixtures.aListOfProperty),
        )
    }
}


