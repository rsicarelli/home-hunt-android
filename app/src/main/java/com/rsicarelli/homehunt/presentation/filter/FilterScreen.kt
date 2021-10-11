package com.rsicarelli.homehunt.presentation.filter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import com.rsicarelli.homehunt.presentation.components.OnLifecycleEvent
import com.rsicarelli.homehunt.presentation.components.Selector
import com.rsicarelli.homehunt.presentation.filter.FilterEvents.BathSelectionChanged
import com.rsicarelli.homehunt.presentation.filter.FilterEvents.DormsSelectionChanged
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

    OnLifecycleEvent { event ->
        events(FilterEvents.LifecycleEvent(event))
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
            stickyHeader {
                IconButton(
                    modifier = Modifier.padding(top = SpaceMedium),
                    onClick = { scaffoldDelegate.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(id = R.string.go_back)
                    )
                }
            }
            item { PriceRange(state, events) }
            item { SurfaceRange(state, events) }
            item { DormSelector(state, events) }
            item { BathSelector(state = state, events = events) }
            item { VisibilitySelector(state = state, events = events) }
        }

        SeeResultsButton(state, events)

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

@Composable
fun VisibilitySelector(state: FilterState, events: (FilterEvents) -> Unit) {
    val selectedItems = mutableListOf<PropertyVisibility>()
    state.seenOnly?.let { selectedItems.add(it) }
    state.notSeenOnly?.let { selectedItems.add(it) }

    Selector(
        titleRes = R.string.visibility,
        items = mapOf("Not seen" to NotSeen, "Seen" to Seen),
        selectedItems = selectedItems,
        onSelectedChanged = {
            events(FilterEvents.VisibilitySelectionChanged(it.second))
        },
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun FilterRange(
    title: String,
    range: ClosedFloatingPointRange<Float>,
    rangeText: String,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
) {
    Spacer(modifier = Modifier.height(SpaceMedium))

    Text(
        text = title,
        style = MaterialTheme.typography.h5
    )

    Spacer(modifier = Modifier.height(SpaceSmallest))

    Text(
        text = rangeText,
        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.W400)
    )

    RangeSlider(
        modifier = Modifier.fillMaxWidth(),
        values = range,
        valueRange = valueRange,
        onValueChange = {
            onValueChange(it)
        })

    Spacer(modifier = Modifier.height(SpaceSmall))

    Divider(thickness = 1.dp)
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
private fun AddOrRemoveItem(
    text: String,
    value: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Spacer(modifier = Modifier.height(SpaceMedium))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1.0f),
            text = text,
            style = MaterialTheme.typography.subtitle1,
        )
        Counter(
            value,
            onIncrease = onIncrease,
            onDecrease = onDecrease,
            contentDescription = text
        )
    }
}

@Composable
private fun Counter(
    value: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    contentDescription: String,
) {
    val isDecreaseEnabled = value != 0
    val isIncreaseEnabled = value != 5

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundButton(
            onDecrease,
            isDecreaseEnabled,
            painter = painterResource(id = R.drawable.ic_round_remove_24),
            contentDescription = contentDescription
        )
        Spacer(modifier = Modifier.width(SpaceMedium))
        Text(text = value.toString(), style = MaterialTheme.typography.body2)
        Spacer(modifier = Modifier.width(SpaceMedium))
        RoundButton(
            onIncrease,
            isIncreaseEnabled,
            Icons.Rounded.Add,
            contentDescription = contentDescription
        )
    }

}

@Composable
private fun RoundButton(
    onClick: () -> Unit,
    enabled: Boolean,
    imageVector: ImageVector? = null,
    painter: Painter? = null,
    contentDescription: String,
) {
    val color =
        if (enabled) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(alpha = 0.3f)

    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .then(Modifier.size(40.dp))
            .border(
                1.dp,
                color = color,
                shape = CircleShape
            )
    ) {

        imageVector?.let {
            Icon(
                it,
                modifier = Modifier.size(20.dp),
                contentDescription = contentDescription,
                tint = color
            )
        }

        painter?.let {
            Icon(
                it,
                modifier = Modifier.size(20.dp),
                contentDescription = contentDescription,
                tint = color
            )
        }

    }
}