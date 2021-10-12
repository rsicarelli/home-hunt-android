package com.rsicarelli.homehunt.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.presentation.components.CircularIndeterminateProgressBar
import com.rsicarelli.homehunt.presentation.components.EmptyContent
import com.rsicarelli.homehunt.presentation.components.LifecycleEffect
import com.rsicarelli.homehunt.presentation.home.components.PropertyList
import com.rsicarelli.homehunt.ui.composition.LocalScaffoldDelegate
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.theme.BottomBarSize
import com.rsicarelli.homehunt.ui.theme.ElevationSize
import com.rsicarelli.homehunt.ui.theme.Size_Small
import com.rsicarelli.homehunt.ui.theme.rally_blue_700

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val scaffoldDelegate = LocalScaffoldDelegate.current

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

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun FilterFab(isScrollInProgress: Boolean, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = !isScrollInProgress,
        enter = expandVertically(expandFrom = Alignment.Top),
        exit = shrinkVertically(shrinkTowards = Alignment.Top)
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = BottomBarSize, end = Size_Small)
        ) {
            FloatingActionButton(
                onClick = onClick,
                elevation = FloatingActionButtonDefaults.elevation(ElevationSize),
                backgroundColor = rally_blue_700
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_filter),
                    contentDescription = stringResource(id = R.string.filter)
                )
            }
        }
    }
}
