package com.rsicarelli.homehunt.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.rsicarelli.homehunt.ui.theme.SpaceSmallest

@Composable
fun IconText(
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.body1,
    paddingLeadingIconEnd: Dp = SpaceSmallest,
    leadingIcon: ImageVector? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(imageVector = leadingIcon, contentDescription = text)
        }
        Text(
            modifier = Modifier.padding(start = paddingLeadingIconEnd),
            text = text,
            style = textStyle
        )
    }
}