package com.rsicarelli.homehunt.presentation.filter.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.rsicarelli.homehunt.ui.theme.BorderSizeSmallest
import com.rsicarelli.homehunt.ui.theme.IconSizeSmall
import com.rsicarelli.homehunt.ui.theme.Size_X_Large

@Composable
fun RoundedButton(
    onClick: () -> Unit,
    enabled: Boolean,
    imageVector: ImageVector? = null,
    painter: Painter? = null,
    contentDescription: String,
) {
    val color =
        if (enabled) MaterialTheme.colors.primary else MaterialTheme.colors.primary.copy(alpha = 0.3f)

    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .then(Modifier.size(Size_X_Large))
            .border(
                BorderSizeSmallest,
                color = color,
                shape = CircleShape
            )
    ) {

        imageVector?.let {
            Icon(
                it,
                modifier = Modifier.size(IconSizeSmall),
                contentDescription = contentDescription,
                tint = color
            )
        }

        painter?.let {
            Icon(
                it,
                modifier = Modifier.size(IconSizeSmall),
                contentDescription = contentDescription,
                tint = color
            )
        }
    }
}