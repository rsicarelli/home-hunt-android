package com.rsicarelli.homehunt.presentation.filter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.presentation.components.BackButton
import com.rsicarelli.homehunt.presentation.components.LifecycleEffect
import com.rsicarelli.homehunt.presentation.filter.components.*
import com.rsicarelli.homehunt.ui.theme.HomeHuntTheme
import com.rsicarelli.homehunt.ui.theme.Size_Large

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterScreen(
    scaffoldDelegate: ScaffoldDelegate,
    viewModel: FilterViewModel = hiltViewModel()
) {
    FilterContent(
        state = viewModel.state.value,
        events = viewModel::onEvent,
        onNavigateSingleTop = { scaffoldDelegate.navigateSingleTop(it) },
        onNavigateUp = { scaffoldDelegate.navigateUp() })
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

    LifecycleEffect { event ->
        events(FilterEvents.LifecycleEvent(event))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
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