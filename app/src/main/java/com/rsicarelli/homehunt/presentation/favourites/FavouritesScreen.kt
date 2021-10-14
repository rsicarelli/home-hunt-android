package com.rsicarelli.homehunt.presentation.favourites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.HomeHuntState
import com.rsicarelli.homehunt.presentation.components.EmptyContent
import com.rsicarelli.homehunt.presentation.home.components.PropertyList
import com.rsicarelli.homehunt.ui.theme.HomeHuntTheme
import utils.Fixtures

@Composable
fun FavouritesScreen(
    homeHuntState: HomeHuntState
) {
    val viewModel: FavouritesViewModel = hiltViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current

    val stateFlowLifecycleAware = remember(viewModel, lifecycleOwner) {
        viewModel.onEvent(FavouritesEvents.GetPropertiesFromCache)
        viewModel.state.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val state by stateFlowLifecycleAware.collectAsState(FavouritesState())

    FavouritesContent(
        events = viewModel::onEvent,
        state = state,
        onNavigate = { route ->
            homeHuntState.navigate(route)
        }
    )
}

@Composable
private fun FavouritesContent(
    events: (FavouritesEvents) -> Unit,
    state: FavouritesState,
    onNavigate: (String) -> Unit,
) {
    EmptyContent(state.emptyResults)

    PropertyList(
        properties = state.properties,
        headerPrefixRes = R.string.favourites,
        onNavigate = onNavigate,
        onToggleFavourite = { referenceId, isFavourited ->
            events(
                FavouritesEvents.ToggleFavourite(
                    referenceId, isFavourited
                )
            )
        }
    )
}

@Composable
@Preview
private fun FavouriteScreenPreview() {
    HomeHuntTheme(isPreview = true) {
        FavouritesContent(
            events = {},
            state = FavouritesState(
                properties = Fixtures.aListOfProperty.map {
                    it.copy(
                        isFavourited = true,
                        isActive = false
                    )
                },
                emptyResults = false
            ),
            onNavigate = {})
    }
}

