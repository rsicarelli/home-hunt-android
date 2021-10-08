package com.rsicarelli.homehunt.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.rsicarelli.homehunt.ui.theme.SpaceSmall
import com.rsicarelli.homehunt.ui.theme.SpaceSmallest

@Composable
fun Tag(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colors.surface,
    style: TextStyle = MaterialTheme.typography.body2
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp)),
        color = color,
        elevation = 8.dp
    ) {
        Text(
            text,
            style = style,
            modifier = Modifier
                .background(Color.Unspecified)
                .padding(
                    start = SpaceSmall,
                    end = SpaceSmall,
                    top = SpaceSmallest,
                    bottom = SpaceSmallest
                )
        )
    }
}