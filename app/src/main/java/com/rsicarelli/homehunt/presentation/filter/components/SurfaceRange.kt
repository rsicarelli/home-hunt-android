package com.rsicarelli.homehunt.presentation.filter.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rsicarelli.homehunt.R

private val valueRange = 0F..300F

@Composable
fun SurfaceRange(
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
) {
    val rangeTextSuffix =
        if (range.endInclusive >= valueRange.endInclusive) "+" else ""

    FilterRange(
        title = stringResource(id = R.string.surface_range),
        range = range,
        valueRange = valueRange,
        rangeText = "${range.start.toInt()}m² - ${range.endInclusive.toInt()}m²$rangeTextSuffix",
        onValueChange = onValueChange
    )
}