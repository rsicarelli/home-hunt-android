package com.rsicarelli.homehunt.presentation.favourites

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.model.toTag
import com.rsicarelli.homehunt.presentation.components.EmptyContent
import com.rsicarelli.homehunt.presentation.components.Tag
import com.rsicarelli.homehunt.presentation.home.components.PropertyList
import com.rsicarelli.homehunt.ui.theme.Size_Small
import com.rsicarelli.homehunt.ui.theme.Size_Regular
import com.rsicarelli.homehunt.ui.theme.rally_orange_300

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
            ListingTags(property = property)
        }
    )
}

@Composable
private fun ListingTags(property: Property) {
    val color = rally_orange_300
    val style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
    val modifier = Modifier.padding(top = Size_Regular)
    val propertyTag = property.tag.toTag()

    if (!property.isActive) {
        Tag(
            text = stringResource(id = R.string.inactive),
            modifier = modifier,
            style = style,
            color = color
        )
    }

    if (propertyTag is Property.Tag.RENTED) {
        Spacer(modifier = Modifier.width(Size_Small))
        Tag(
            text = stringResource(id = R.string.rented),
            modifier = modifier,
            style = style,
            color = color
        )
    }

    if (propertyTag is Property.Tag.RESERVED) {
        Spacer(modifier = Modifier.width(Size_Small))
        Tag(
            text = stringResource(id = R.string.reserved),
            modifier = modifier,
            style = style,
            color = color
        )
    }
}

