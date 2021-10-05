package com.rsicarelli.homehunt.presentation.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.RangeSlider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.core.content.res.ResourcesCompat
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.ui.theme.SpaceBiggest
import com.rsicarelli.homehunt.ui.theme.SpaceMedium
import com.rsicarelli.homehunt.ui.theme.SpaceNormal
import com.rsicarelli.homehunt.ui.theme.SpaceSmall

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomRangeSlider(
    values: List<String>,
    value: ClosedFloatingPointRange<Float>,
    onValueChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    val (sliderValue, setSliderValue) = remember { mutableStateOf(value) }
    val drawPadding = with(LocalDensity.current) { SpaceMedium.toPx() }
    val lineHeightPx = with(LocalDensity.current) { SpaceSmall.toPx() }
    val textPaint = Paint().apply {
        isAntiAlias = true
        color = if (isSystemInDarkTheme()) 0xffffffff.toInt() else 0xffff47586B.toInt()
        textAlign = Paint.Align.CENTER
        this.textSize = with(LocalDensity.current) { SpaceNormal.toPx() }
        typeface = ResourcesCompat.getFont(LocalContext.current, R.font.roboto_condensed_regular)
    }

    Box(contentAlignment = Alignment.Center) {
        Canvas(
            modifier = Modifier
                .height(SpaceBiggest + SpaceSmall)
                .fillMaxWidth()
        ) {
            val yStart = 0f
            val distance = (size.width.minus(2 * drawPadding)).div(values.size.minus(1))
            values.forEachIndexed { index, step ->
                drawLine(
                    color = Color.DarkGray,
                    start = Offset(x = drawPadding + index.times(distance), y = yStart),
                    end = Offset(x = drawPadding + index.times(distance), y = lineHeightPx)
                )
                if (index.rem(3) == 0) {
                    this.drawContext.canvas.nativeCanvas.drawText(
                        step,
                        drawPadding + index.times(distance),
                        size.height,
                        textPaint
                    )
                }
            }
        }
        RangeSlider(
            modifier = Modifier.fillMaxWidth(),
            values = sliderValue,
            valueRange = 0f..values.size.minus(1).toFloat(),
            steps = values.size.minus(2),
            onValueChange = {
                setSliderValue(it)
                onValueChange(it)
            })
    }
}
