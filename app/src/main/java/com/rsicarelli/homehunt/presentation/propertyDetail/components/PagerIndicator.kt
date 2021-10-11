package com.rsicarelli.homehunt.presentation.propertyDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rsicarelli.homehunt.ui.theme.SpaceMedium

@Composable
fun PagerIndicator(
    currentPage: Int,
    totalItems: Int
) {
    Box(
        modifier = Modifier
            .height(36.dp)
            .background(
                color = MaterialTheme.colors.background.copy(alpha = 0.8f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(
                start = SpaceMedium,
                end = SpaceMedium
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            style = MaterialTheme.typography.button,
            text = "${currentPage + 1} of $totalItems"
        )
    }
}