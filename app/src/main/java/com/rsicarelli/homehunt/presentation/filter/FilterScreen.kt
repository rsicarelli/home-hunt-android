package com.rsicarelli.homehunt.presentation.filter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.insets.systemBarsPadding
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.domain.model.PropertyVisibility
import com.rsicarelli.homehunt.domain.model.PropertyVisibility.NotSeen
import com.rsicarelli.homehunt.domain.model.PropertyVisibility.Seen
import com.rsicarelli.homehunt.presentation.components.CustomRangeSlider
import com.rsicarelli.homehunt.presentation.components.OnLifecycleEvent
import com.rsicarelli.homehunt.presentation.components.Selector
import com.rsicarelli.homehunt.ui.theme.*

val priceRange = mapOf(
    "700" to 700.0,
    "800" to 800.0,
    "900" to 900.0,
    "1000" to 1000.0,
    "1100" to 1100.0,
    "1200" to 1200.0,
    "1300" to 1300.0,
    "1400" to 1400.0,
    "1500" to 1500.0,
    "1600 +" to 1600.0
)

val surfaceRange = mapOf(
    "80m²" to 80.0,
    "90m²" to 90.0,
    "100m²" to 100.0,
    "110m²" to 110.0,
    "120m²" to 120.0,
    "130m²" to 130.0,
    "140m²" to 140.0,
    "150m²" to 150.0,
    "160m²" to 160.0,
    "180m² +" to 180.0
)

val countRange = mapOf("1" to 1, "2" to 2, "3" to 3, "4" to 4, "5" to 5)

val visibilityRange = mapOf("Not seen" to NotSeen, "Seen" to Seen)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterScreen(
    scaffoldDelegate: ScaffoldDelegate,
    viewModel: FilterViewModel = hiltViewModel()
) {
    FilterContent(viewModel.state.value, scaffoldDelegate, viewModel::onEvent)
}

@Composable
private fun FilterContent(
    state: FilterState,
    scaffoldDelegate: ScaffoldDelegate,
    events: (FilterEvents) -> Unit
) {
    if (state.uiEvent is UiEvent.Navigate) scaffoldDelegate.navigateSingleTop(state.uiEvent)

    OnLifecycleEvent { event ->
        events(FilterEvents.LifecycleEvent(event))
    }

    IconButton(
        modifier = Modifier.padding(top = SpaceMedium, start = SpaceSmall),
        onClick = { scaffoldDelegate.navigateUp() }) {
        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = "back")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SpaceLarge)
    ) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .weight(1.0f)
        ) {
            item { PriceRange(state, events) }
            item { SurfaceRange(state, events) }
            item { DormSelector(state = state, events = events) }
            item {
                VisibilitySelector(state = state, events = events)
            }
            item { BathSelector(state = state, events = events) }
            item {

            }
        }

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
                    .height(48.dp),
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
}

@Composable
fun VisibilitySelector(state: FilterState, events: (FilterEvents) -> Unit) {
    val selectedItems = mutableListOf<PropertyVisibility>()
    state.seenOnly?.let { selectedItems.add(it) }
    state.notSeenOnly?.let { selectedItems.add(it) }

    Selector(
        titleRes = R.string.visibility,
        items = visibilityRange,
        selectedItems = selectedItems,
        onSelectedChanged = {
            events(FilterEvents.VisibilitySelectionChanged(it.second))
        },
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    )
}

@Composable
private fun SurfaceRange(
    state: FilterState,
    events: (FilterEvents) -> Unit
) {
    Spacer(modifier = Modifier.height(SpaceLarger))

    Text(
        text = stringResource(id = R.string.surface),
        style = MaterialTheme.typography.h6
    )
    CustomRangeSlider(
        values = surfaceRange,
        value = state.surfaceRange,
        onValueChange = { events(FilterEvents.SurfaceRangeChanged(it)) }
    )
}

@Composable
private fun PriceRange(
    state: FilterState,
    events: (FilterEvents) -> Unit
) {
    Spacer(modifier = Modifier.height(SpaceBiggest))

    Text(
        text = stringResource(id = R.string.price),
        style = MaterialTheme.typography.h6
    )
    CustomRangeSlider(
        values = priceRange,
        value = state.priceRange,
        onValueChange = { events(FilterEvents.PriceRangeChanged(it)) }
    )
}

@Composable
private fun DormSelector(
    state: FilterState,
    events: (FilterEvents) -> Unit
) {
    Selector(
        titleRes = R.string.dorm_count,
        items = countRange,
        selectedItems = state.selectedDorms,
        onSelectedChanged = {
            events(FilterEvents.DormsSelectionChanged(it.second))
        })
}

@Composable
private fun BathSelector(
    state: FilterState,
    events: (FilterEvents) -> Unit
) {
    Selector(
        titleRes = R.string.bath_count,
        items = countRange,
        selectedItems = state.selectedBaths,
        onSelectedChanged = {
            events(FilterEvents.BathSelectionChanged(it.second))
        })
}