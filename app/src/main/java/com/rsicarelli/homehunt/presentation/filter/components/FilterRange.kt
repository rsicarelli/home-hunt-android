package com.rsicarelli.homehunt.presentation.filter.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rsicarelli.homehunt.ui.theme.SpaceMedium
import com.rsicarelli.homehunt.ui.theme.SpaceSmall
import com.rsicarelli.homehunt.ui.theme.SpaceSmallest


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterRange(
    title: String,
    range: ClosedFloatingPointRange<Float>,
    rangeText: String,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
) {
    Spacer(modifier = Modifier.height(SpaceMedium))

    Text(
        text = title,
        style = MaterialTheme.typography.h6
    )

    Spacer(modifier = Modifier.height(SpaceSmallest))

    Text(
        text = rangeText,
        style = MaterialTheme.typography.subtitle1
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