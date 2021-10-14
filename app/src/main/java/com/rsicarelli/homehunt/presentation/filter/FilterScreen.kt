package com.rsicarelli.homehunt.presentation.filter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.google.accompanist.insets.statusBarsPadding
import com.rsicarelli.homehunt.core.model.HomeHuntState
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.presentation.components.BackButton
import com.rsicarelli.homehunt.presentation.components.LifecycleEffect
import com.rsicarelli.homehunt.presentation.filter.components.*
import com.rsicarelli.homehunt.presentation.home.HomeEvents
import com.rsicarelli.homehunt.presentation.home.HomeState
import com.rsicarelli.homehunt.presentation.home.HomeViewModel
import com.rsicarelli.homehunt.ui.theme.HomeHuntTheme
import com.rsicarelli.homehunt.ui.theme.Size_Large

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterScreen(
    homeHuntState: HomeHuntState,
) {
    val viewModel: FilterViewModel = hiltViewModel()
    val lifecycleOwner = LocalLifecycleOwner.current

    val stateFlowLifecycleAware = remember(viewModel, lifecycleOwner) {
        viewModel.onEvent(FilterEvents.GetFilter)
        viewModel.state.flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    val state by stateFlowLifecycleAware.collectAsState(FilterState())

    FilterContent(
        state = state,
        events = viewModel::onEvent,
        onNavigateSingleTop = { homeHuntState.navigateSingleTop(it) },
        onNavigateUp = { homeHuntState.navigateUp() })
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FilterContent(
    state: FilterState,
    events: (FilterEvents) -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateSingleTop: (String) -> Unit
) {
    if (state.uiEvent is UiEvent.Navigate) onNavigateSingleTop(state.uiEvent.route)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    ) {
        BackButton(
            modifier = Modifier,
            onBackClick = onNavigateUp
        )

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .weight(1.0f)
                .padding(start = Size_Large, end = Size_Large),
        ) {
            item {
                PriceRange(
                    range = state.priceRange,
                    onValueChange = {
                        events(FilterEvents.PriceRangeChanged(it))
                    }
                )
            }
            item {
                SurfaceRange(
                    range = state.surfaceRange,
                    onValueChange = {
                        events(FilterEvents.SurfaceRangeChanged(it))
                    }
                )
            }
            item {
                DormSelector(
                    dormCount = state.dormCount,
                    onValueChanged = { newValue ->
                        events(FilterEvents.DormsSelectionChanged(newValue))
                    }
                )
            }
            item {
                BathSelector(
                    bathCount = state.bathCount,
                    onValueChanged = { newValue ->
                        events(FilterEvents.BathSelectionChanged(newValue))
                    }
                )
            }
            item {
                VisibilitySelector(
                    isChecked = state.showSeen,
                    onChange = {
                        events(FilterEvents.VisibilitySelectionChanged(it))
                    }
                )
            }
            item {
                LongTermRentalSelector(
                    isChecked = state.longTermOnly,
                    onChange = {
                        events(FilterEvents.LongerTermRentalSelectionChanged(it))
                    }
                )
            }
            item {
                AvailabilitySelector(
                    isChecked = state.availableOnly,
                    onChange = {
                        events(FilterEvents.AvailabilitySelectionChanged(it))
                    }
                )
            }
        }

        SeeResultsButton(
            previewResultCount = state.previewResultCount,
            onClick = {
                events(FilterEvents.SaveFilter)
            }
        )
    }
}

@Composable
@Preview
private fun FilterContentPreview() {
    HomeHuntTheme(isPreview = true) {
        FilterContent(
            state = FilterState(),
            events = {},
            onNavigateUp = {},
            onNavigateSingleTop = {}
        )
    }
}