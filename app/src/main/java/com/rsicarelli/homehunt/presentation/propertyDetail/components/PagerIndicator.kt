package com.rsicarelli.homehunt.presentation.propertyDetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.rsicarelli.homehunt.ui.theme.Size_Regular
import com.rsicarelli.homehunt.ui.theme.Size_X_Large

@Composable
fun PagerIndicator(
    currentPage: Int,
    totalItems: Int
) {
    Box(
        modifier = Modifier
            .height(Size_X_Large)
            .background(
                color = MaterialTheme.colors.background.copy(alpha = 0.8f),
                shape = MaterialTheme.shapes.large
            )
            .padding(
                start = Size_Regular,
                end = Size_Regular
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            style = MaterialTheme.typography.button,
            text = "${currentPage + 1} of $totalItems"
        )
    }
}