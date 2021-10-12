package com.rsicarelli.homehunt.presentation.filter.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rsicarelli.homehunt.R

@Composable
fun DormSelector(
    dormCount: Int,
    onValueChanged: (Int) -> Unit
) {
    AddOrRemoveItem(
        text = stringResource(id = R.string.bedrooms),
        value = dormCount,
        onIncrease = { onValueChanged(dormCount + 1) },
        onDecrease = { onValueChanged(dormCount - 1) }
    )
}