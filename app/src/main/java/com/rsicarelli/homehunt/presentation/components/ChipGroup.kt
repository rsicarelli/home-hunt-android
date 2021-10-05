package com.rsicarelli.homehunt.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rsicarelli.homehunt.ui.theme.SpaceLarge
import com.rsicarelli.homehunt.ui.theme.SpaceMedium

@Composable
fun ChipGroup(
    items: List<Int>,
    selectedItem: List<Int?> = listOf(),
    onSelectedChanged: (String) -> Unit = {},
) {
    LazyRow(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(items) {
            Chip(
                text = it.toString(),
                isSelected = selectedItem.contains(it),
                onSelectionChanged = {
                    onSelectedChanged(it)
                },
            )
        }
    }
}

@Composable
fun Chip(
    text: String = "Chip",
    isSelected: Boolean = false,
    onSelectionChanged: (String) -> Unit = {},
) {
    Surface(
        elevation = SpaceMedium,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) Color.LightGray else MaterialTheme.colors.surface
    ) {
        Row(modifier = Modifier
            .toggleable(
                value = isSelected,
                onValueChange = { onSelectionChanged(text) }
            )
            .height(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                color = Color.White,
                modifier = Modifier
                    .padding(
                        end = SpaceLarge,
                        start = SpaceLarge
                    )
            )
        }
    }
}