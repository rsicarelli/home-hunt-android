package com.rsicarelli.homehunt.presentation.filter.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.CheckboxDefaults.colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.rsicarelli.homehunt.ui.theme.SpaceMedium
import com.rsicarelli.homehunt.ui.theme.SpaceSmall
import com.rsicarelli.homehunt.ui.theme.SpaceSmallest

@Composable
fun CheckboxItem(
    title: String,
    isChecked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onCheckedChange(!isChecked)
            }
            .padding(top = SpaceSmall, bottom = SpaceSmall, end = SpaceSmallest),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1.0f),
            text = title,
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.W400),
        )
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = colors(
                checkedColor = MaterialTheme.colors.primary
            )
        )
    }
}