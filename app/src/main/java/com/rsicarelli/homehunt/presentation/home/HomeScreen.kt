package com.rsicarelli.homehunt.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.presentation.components.CircularIndeterminateProgressBar
import com.rsicarelli.homehunt.presentation.components.EmptyContent
import com.rsicarelli.homehunt.presentation.components.LifecycleEffect
import com.rsicarelli.homehunt.presentation.home.components.FilterFab
import com.rsicarelli.homehunt.presentation.home.components.PropertyList
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.theme.HomeHuntTheme
import utils.Fixtures

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    scaffoldDelegate: ScaffoldDelegate,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    HomeContent(
        events = viewModel::onEvent,
        state = viewModel.state.value,
        onNavigate = { route ->
            scaffoldDelegate.navigate(route)
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
    LifecycleEffect { event ->
        events(HomeEvents.LifecycleEvent(event))
    }

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


