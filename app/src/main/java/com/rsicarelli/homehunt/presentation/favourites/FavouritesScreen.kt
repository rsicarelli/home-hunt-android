package com.rsicarelli.homehunt.presentation.favourites

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.presentation.components.EmptyContent
import com.rsicarelli.homehunt.presentation.home.components.PropertyList


@Composable
fun FavouritesScreen(
    scaffoldDelegate: ScaffoldDelegate,
    viewModel: FavouritesViewModel = hiltViewModel(),
    imageLoader: ImageLoader
) {
    FavouritesContent(
        scaffoldDelegate = scaffoldDelegate,
        imageLoader = imageLoader,
        events = viewModel::onEvent,
        state = viewModel.state.value
    )
}

@Composable
private fun FavouritesContent(
    events: (FavouritesEvents) -> Unit,
    state: FavouritesState,
    imageLoader: ImageLoader,
    scaffoldDelegate: ScaffoldDelegate
) {

    EmptyContent(state.emptyResults)

    PropertyList(
        properties = state.properties,
        imageLoader = imageLoader,
        showFab = false,
        headerPrefixRes = R.string.favourites,
        scaffoldDelegate = scaffoldDelegate,
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
