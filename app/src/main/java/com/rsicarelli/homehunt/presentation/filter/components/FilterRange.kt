package com.rsicarelli.homehunt.presentation.filter.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rsicarelli.homehunt.ui.theme.DividerSize
import com.rsicarelli.homehunt.ui.theme.Size_X_Small
import com.rsicarelli.homehunt.ui.theme.Size_Small
import com.rsicarelli.homehunt.ui.theme.Size_Regular


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterRange(
    title: String,
    range: ClosedFloatingPointRange<Float>,
    rangeText: String,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit,
) {
    Spacer(modifier = Modifier.height(Size_Regular))

    Text(
        text = title,
        style = MaterialTheme.typography.h6
    )

    Spacer(modifier = Modifier.height(Size_X_Small))

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

    Spacer(modifier = Modifier.height(Size_Small))

    Divider(thickness = DividerSize)
}