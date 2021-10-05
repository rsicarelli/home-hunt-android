package com.rsicarelli.homehunt.presentation.filter

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.presentation.components.ChipGroup
import com.rsicarelli.homehunt.presentation.components.CustomRangeSlider
import com.rsicarelli.homehunt.ui.theme.*


val priceRange =
    listOf("700", "800", "900", "1000", "1100", "1200", "1300", "1400", "1500", "1600 +")
val surfaceRange =
    listOf("80m²", "90m²", "100m²", "110m²", "120m²", "130m²", "140m²", "150m²", "160m²", "180m² +")
val countRange = listOf(1, 2, 3, 4, 5)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterScreen(
    scaffoldDelegate: ScaffoldDelegate,
    state: FilterState,
    events: (FilterEvents) -> Unit
) {
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
        Spacer(modifier = Modifier.height(SpaceBiggest))

        PriceRange(state, events)

        Spacer(modifier = Modifier.height(SpaceLarger))

        SurfaceRange(state, events)

        Spacer(modifier = Modifier.height(SpaceLarger))

        DormSelector(state = state, events = events)

        Spacer(modifier = Modifier.height(SpaceLarger))

        BathSelector(state = state, events = events)

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .height(48.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = rally_green_500,
                    contentColor = MaterialTheme.colors.background
                ),
                onClick = { }) {
                Text(text = "See ### results",style = MaterialTheme.typography.button.copy(fontSize = 16.sp))
            }
        }

    }
}

@Composable
private fun SurfaceRange(
    state: FilterState,
    events: (FilterEvents) -> Unit
) {
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
fun DormSelector(
    state: FilterState,
    events: (FilterEvents) -> Unit
) {
    Text(
        text = stringResource(id = R.string.dorm_count),
        style = MaterialTheme.typography.h6
    )
    Spacer(modifier = Modifier.height(SpaceMedium))
    ChipGroup(
        items = countRange,
        selectedItem = state.selectedDorms,
        onSelectedChanged = {
            events(FilterEvents.DormsSelectedChange(it.toInt()))
        }
    )
}

@Composable
fun BathSelector(
    state: FilterState,
    events: (FilterEvents) -> Unit
) {
    Text(
        text = stringResource(id = R.string.bath_count),
        style = MaterialTheme.typography.h6
    )
    Spacer(modifier = Modifier.height(SpaceMedium))
    ChipGroup(
        items = countRange,
        selectedItem = state.selectedBaths,
        onSelectedChanged = {
            events(FilterEvents.BathSelectedChange(it.toInt()))
        }
    )
}