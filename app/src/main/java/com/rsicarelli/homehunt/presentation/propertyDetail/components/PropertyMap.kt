package com.rsicarelli.homehunt.presentation.propertyDetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import com.rsicarelli.homehunt.presentation.components.MapView
import com.rsicarelli.homehunt.ui.theme.SpaceMedium

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PropertyMap(
    lat: Double?,
    lng: Double?,
    onMapClick: () -> Unit
) {
    if (lat == null || lng == null || lat == 0.0 || lng == 0.0) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = SpaceMedium, end = SpaceMedium)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            MapView(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMapClick() },
                lat = lat,
                lng = lng,
                isLiteMode = true
            )
        }

    }
}