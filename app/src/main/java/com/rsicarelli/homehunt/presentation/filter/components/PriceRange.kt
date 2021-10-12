package com.rsicarelli.homehunt.presentation.filter.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rsicarelli.homehunt.R

private val rangeValue = 0F..2000F

@Composable
fun PriceRange(
    range: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
) {
    val rangeTextSuffix = if (range.endInclusive >= rangeValue.endInclusive) "+" else ""

    FilterRange(
        title = stringResource(id = R.string.price_range),
        range = range,
        valueRange = rangeValue,
        rangeText = "€${range.start.toInt()} - €${range.endInclusive.toInt()}$rangeTextSuffix",
        onValueChange = onValueChange
    )
}

