package com.rsicarelli.homehunt.presentation.filter.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rsicarelli.homehunt.R

@Composable
fun BathSelector(
    bathCount: Int,
    onValueChanged: (Int) -> Unit,
) {
    AddOrRemoveItem(
        text = stringResource(id = R.string.bathrooms),
        value = bathCount,
        onIncrease = { onValueChanged(bathCount + 1) },
        onDecrease = { onValueChanged(bathCount - 1) }
    )
}