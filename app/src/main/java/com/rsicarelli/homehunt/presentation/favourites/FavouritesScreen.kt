package com.rsicarelli.homehunt.presentation.favourites

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.domain.model.toTag
import com.rsicarelli.homehunt.ui.components.AppScaffold
import com.rsicarelli.homehunt.presentation.components.EmptyContent
import com.rsicarelli.homehunt.presentation.components.ListingTag
import com.rsicarelli.homehunt.presentation.home.components.PropertyList
import com.rsicarelli.homehunt.ui.composition.LocalScaffoldDelegate
import com.rsicarelli.homehunt.ui.theme.HomeHuntTheme
import utils.Fixtures

@Composable
fun FavouritesScreen(
    viewModel: FavouritesViewModel = hiltViewModel()
) {
    val scaffoldDelegate = LocalScaffoldDelegate.current

    FavouritesContent(
        events = viewModel::onEvent,
        state = viewModel.state.value,
        onNavigate = { route ->
            scaffoldDelegate.navigate(route)
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
        onToggleFavourite = { property ->
            events(
                FavouritesEvents.ToggleFavourite(
                    property.reference,
                    !property.isFavourited
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

