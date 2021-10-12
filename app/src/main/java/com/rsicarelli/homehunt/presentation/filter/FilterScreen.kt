package com.rsicarelli.homehunt.presentation.filter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.presentation.components.BackButton
import com.rsicarelli.homehunt.presentation.components.LifecycleEffect
import com.rsicarelli.homehunt.presentation.filter.FilterEvents.BathSelectionChanged
import com.rsicarelli.homehunt.presentation.filter.FilterEvents.DormsSelectionChanged
import com.rsicarelli.homehunt.presentation.filter.components.AddOrRemoveItem
import com.rsicarelli.homehunt.presentation.filter.components.CheckboxItem
import com.rsicarelli.homehunt.presentation.filter.components.FilterRange
import com.rsicarelli.homehunt.ui.theme.*

private val priceRange = 0F..2000F
private val surfaceRange = 0F..300F

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
            item { PriceRange(state = state, events = events) }
            item { SurfaceRange(state = state, events = events) }
            item { DormSelector(state = state, events = events) }
            item { BathSelector(state = state, events = events) }
            item { VisibilitySelector(state = state, events = events) }
            item { LongTermRentalSelector(state = state, events = events) }
        }

        SeeResultsButton(state = state, events = events)
    }
}

@Composable
fun PriceRange(state: FilterState, events: (FilterEvents) -> Unit) {
    val rangeTextSuffix =
        if (state.priceRange.endInclusive >= priceRange.endInclusive) "+" else ""

    FilterRange(
        title = stringResource(id = R.string.price_range),
        range = state.priceRange,
        valueRange = priceRange,
        rangeText = "€${state.priceRange.start.toInt()} - €${state.priceRange.endInclusive.toInt()}$rangeTextSuffix",
        onValueChange = {
            events(FilterEvents.PriceRangeChanged(it))
        })
}

@Composable
private fun SurfaceRange(
    state: FilterState,
    events: (FilterEvents) -> Unit
) {
    val rangeTextSuffix =
        if (state.surfaceRange.endInclusive >= surfaceRange.endInclusive) "+" else ""

    FilterRange(
        title = stringResource(id = R.string.surface_range),
        range = state.surfaceRange,
        valueRange = surfaceRange,
        rangeText = "${state.surfaceRange.start.toInt()}m² - ${state.surfaceRange.endInclusive.toInt()}m²$rangeTextSuffix",
        onValueChange = {
            events(FilterEvents.SurfaceRangeChanged(it))
        })
}

@Composable
fun VisibilitySelector(
    state: FilterState,
    events: (FilterEvents) -> Unit
) {
    Spacer(modifier = Modifier.height(Size_Small))

    CheckboxItem(
        title = stringResource(id = R.string.show_seen),
        isChecked = state.showSeen,
        onCheckedChange = {
            events(FilterEvents.VisibilitySelectionChanged(it))
        },
    )
}

@Composable
fun LongTermRentalSelector(
    state: FilterState,
    events: (FilterEvents) -> Unit
) {
    Spacer(modifier = Modifier.height(Size_X_Small))

    CheckboxItem(
        title = stringResource(id = R.string.show_longer_term_only),
        isChecked = state.longTermOnly,
        onCheckedChange = {
            events(FilterEvents.LongerTermRentalSelectionChanged(it))
        },
    )
}


@Composable
private fun DormSelector(
    state: FilterState,
    events: (FilterEvents) -> Unit
) {
    AddOrRemoveItem(
        text = stringResource(id = R.string.bedrooms),
        value = state.dormCount,
        onIncrease = { events(DormsSelectionChanged(state.dormCount + 1)) },
        onDecrease = { events(DormsSelectionChanged(state.dormCount - 1)) }
    )
}

@Composable
private fun BathSelector(
    state: FilterState,
    events: (FilterEvents) -> Unit
) {
    AddOrRemoveItem(
        text = stringResource(id = R.string.bathrooms),
        value = state.bathCount,
        onIncrease = { events(BathSelectionChanged(state.bathCount + 1)) },
        onDecrease = { events(BathSelectionChanged(state.bathCount - 1)) }
    )
}

@Composable
private fun SeeResultsButton(
    state: FilterState,
    events: (FilterEvents) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        val hasResults = state.previewResultCount != null
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(Size_2X_Large),
            shape = MaterialTheme.shapes.large,
            enabled = hasResults,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = rally_green_500,
                contentColor = MaterialTheme.colors.background
            ),
            onClick = { events(FilterEvents.SaveFilter) })
        {
            val resources = LocalContext.current.resources

            val text = state.previewResultCount?.let {
                resources.getQuantityString(
                    R.plurals.see_results_plurals, it, it
                )
            } ?: stringResource(id = R.string.calculating_results)

            Text(
                text = text,
                style = MaterialTheme.typography.button.copy(fontSize = 16.sp)
            )
        }
    }
}