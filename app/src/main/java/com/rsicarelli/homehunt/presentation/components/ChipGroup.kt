package com.rsicarelli.homehunt.presentation.components

import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rsicarelli.homehunt.ui.theme.*


@Composable
fun <T> Selector(
    @StringRes titleRes: Int,
    items: Map<String, T>,
    selectedItems: List<T>,
    onSelectedChanged: (Pair<String, T>) -> Unit,
    horizontalArrangement: Arrangement.HorizontalOrVertical = Arrangement.SpaceBetween
) {
    Spacer(modifier = Modifier.height(SpaceLarge))

    Text(
        text = stringResource(id = titleRes),
        style = MaterialTheme.typography.h6
    )
    Spacer(modifier = Modifier.height(SpaceMedium))
    ChipGroup(
        items = items,
        selectedItem = selectedItems,
        onSelectedChanged = onSelectedChanged,
        horizontalArrangement
    )
}

@Composable
private fun <T> ChipGroup(
    items: Map<String, T>,
    selectedItem: List<T> = listOf(),
    onSelectedChanged: (Pair<String, T>) -> Unit = {},
    horizontalArrangement: Arrangement.HorizontalOrVertical = Arrangement.SpaceBetween
) {
    LazyRow(
        Modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement
    ) {
        items(items.keys.toList()) { itemKey ->
            val value = items[itemKey]

            Chip(
                item = Pair(itemKey, items[itemKey]),
                isSelected = selectedItem.contains(value),
                onSelectionChanged = { item ->
                    //For some reason the generic is returning nullable :thinking:
                    item.second?.let {
                        onSelectedChanged(Pair(item.first, it))
                    }
                },
            )
        }
    }
}

@Composable
fun <T> Chip(
    item: Pair<String, T>,
    isSelected: Boolean = false,
    onSelectionChanged: (Pair<String, T>) -> Unit = {},
) {
    Box(Modifier.padding(start = 4.dp, end = 4.dp)) {
        Surface(
            elevation = 6.dp,
            shape = MaterialTheme.shapes.medium,
            color = if (isSelected) Color.LightGray else MaterialTheme.colors.surface
        ) {
            Row(modifier = Modifier
                .toggleable(
                    value = isSelected,
                    onValueChange = {
                        onSelectionChanged(item)
                    }
                )
                .height(48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.first,
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
}