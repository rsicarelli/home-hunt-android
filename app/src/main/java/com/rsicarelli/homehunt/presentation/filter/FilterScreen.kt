package com.rsicarelli.homehunt.presentation.filter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.presentation.components.BackButton
import com.rsicarelli.homehunt.presentation.components.LifecycleEffect
import com.rsicarelli.homehunt.presentation.filter.components.*
import com.rsicarelli.homehunt.ui.theme.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterScreen(
    scaffoldDelegate: ScaffoldDelegate,
    viewModel: FilterViewModel = hiltViewModel()
) {
    FilterContent(viewModel.state.value, scaffoldDelegate, viewModel::onEvent)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FilterContent(
    state: FilterState,
    scaffoldDelegate: ScaffoldDelegate,
    events: (FilterEvents) -> Unit
) {
    if (state.uiEvent is UiEvent.Navigate) scaffoldDelegate.navigateSingleTop(state.uiEvent)

    LifecycleEffect { event ->
        events(FilterEvents.LifecycleEvent(event))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Size_Large)
    ) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .weight(1.0f)
        ) {
            stickyHeader {
                BackButton(
                    modifier = Modifier.padding(top = Size_Regular),
                    onBackClick = { scaffoldDelegate.navigateUp() })
            }
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
        }

        SeeResultsButton(
            previewResultCount = state.previewResultCount,
            onClick = {
                events(FilterEvents.SaveFilter)
            }
        )
    }
}