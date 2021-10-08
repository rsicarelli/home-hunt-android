package com.rsicarelli.homehunt.presentation.home

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.presentation.components.CircularIndeterminateProgressBar
import com.rsicarelli.homehunt.presentation.components.EmptyContent
import com.rsicarelli.homehunt.presentation.components.OnLifecycleEvent
import com.rsicarelli.homehunt.presentation.home.components.PropertyList

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    scaffoldDelegate: ScaffoldDelegate,
    viewModel: HomeViewModel = hiltViewModel(),
    imageLoader: ImageLoader
) {
    HomeContent(viewModel::onEvent, viewModel.state.value, imageLoader, scaffoldDelegate)
}

@Composable
private fun HomeContent(
    events: (HomeEvents) -> Unit,
    state: HomeState,
    imageLoader: ImageLoader,
    scaffoldDelegate: ScaffoldDelegate
) {
    OnLifecycleEvent { event ->
        events(HomeEvents.LifecycleEvent(event))
    }

    EmptyContent(state.emptyResults)

    PropertyList(
        properties = state.properties,
        imageLoader = imageLoader,
        showFab = true,
        headerPrefixRes = R.string.results,
        scaffoldDelegate = scaffoldDelegate,
        onToggleFavourite = { property ->
            events(
                HomeEvents.ToggleFavourite(
                    property.reference,
                    !property.isFavourited
                )
            )
        }
    )

    CircularIndeterminateProgressBar(state.progressBarState)
}
