package com.rsicarelli.homehunt.presentation.home

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.presentation.components.CircularIndeterminateProgressBar
import com.rsicarelli.homehunt.presentation.components.EmptyContent
import com.rsicarelli.homehunt.presentation.components.OnLifecycleEvent
import com.rsicarelli.homehunt.presentation.home.components.PropertyList
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.theme.SpaceLarge
import com.rsicarelli.homehunt.ui.theme.SpaceSmall
import com.rsicarelli.homehunt.ui.theme.rally_blue_700

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    scaffoldDelegate: ScaffoldDelegate,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    HomeContent(viewModel::onEvent, viewModel.state.value, scaffoldDelegate)
}

@Composable
private fun HomeContent(
    events: (HomeEvents) -> Unit,
    state: HomeState,
    scaffoldDelegate: ScaffoldDelegate
) {
    val scrollState = rememberLazyListState()
    OnLifecycleEvent { event ->
        events(HomeEvents.LifecycleEvent(event))
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        EmptyContent(state.emptyResults)

        PropertyList(
            scrollState = scrollState,
            properties = state.properties,
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

        FilterFab(scrollState = scrollState) {
            scaffoldDelegate.navigate(UiEvent.Navigate(Screen.Filter.route))
        }

        CircularIndeterminateProgressBar(state.progressBarState)
    }

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun FilterFab(scrollState: LazyListState, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = !scrollState.isScrollInProgress,
        enter = slideInVertically() + expandVertically(
            expandFrom = Alignment.Bottom
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 40.dp + SpaceLarge, end = SpaceSmall)
                .systemBarsPadding()
        ) {
            FloatingActionButton(
                onClick = onClick,
                elevation = FloatingActionButtonDefaults.elevation(10.dp),
                backgroundColor = rally_blue_700
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_filter),
                    contentDescription = "Filter"
                )
            }
        }

    }
}
