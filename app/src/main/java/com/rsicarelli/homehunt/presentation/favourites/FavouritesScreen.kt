package com.rsicarelli.homehunt.presentation.favourites

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.domain.model.toTag
import com.rsicarelli.homehunt.presentation.components.EmptyContent
import com.rsicarelli.homehunt.presentation.components.ListingTag
import com.rsicarelli.homehunt.presentation.home.components.PropertyList

@Composable
fun FavouritesScreen(
    scaffoldDelegate: ScaffoldDelegate,
    viewModel: FavouritesViewModel = hiltViewModel()
) {
    FavouritesContent(
        scaffoldDelegate = scaffoldDelegate,
        events = viewModel::onEvent,
        state = viewModel.state.value
    )
}

@Composable
private fun FavouritesContent(
    events: (FavouritesEvents) -> Unit,
    state: FavouritesState,
    scaffoldDelegate: ScaffoldDelegate
) {

    EmptyContent(state.emptyResults)

    PropertyList(
        properties = state.properties,
        headerPrefixRes = R.string.favourites,
        scaffoldDelegate = scaffoldDelegate,
        onToggleFavourite = { property ->
            events(
                FavouritesEvents.ToggleFavourite(
                    property.reference,
                    !property.isFavourited
                )
            )
        },
        extraContent = { property ->
            ListingTag(
                isPropertyActive = property.isActive,
                propertyTag = property.tag.toTag()
            )
        }
    )
}

