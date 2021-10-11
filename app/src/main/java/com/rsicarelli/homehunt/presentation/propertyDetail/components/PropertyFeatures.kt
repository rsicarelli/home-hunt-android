package com.rsicarelli.homehunt.presentation.propertyDetail.components

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rsicarelli.homehunt.presentation.components.ChipVerticalGrid
import com.rsicarelli.homehunt.presentation.components.Tag
import com.rsicarelli.homehunt.ui.theme.SpaceMedium

@Composable
fun PropertyFeatures(characteristics: List<String>) {
    ChipVerticalGrid(
        spacing = 7.dp,
        modifier = Modifier
            .padding(start = SpaceMedium, end = SpaceMedium, bottom = SpaceMedium)
    ) {
        characteristics.forEach { word ->
            Tag(text = word)
        }
    }
}