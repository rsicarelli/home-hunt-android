package com.rsicarelli.homehunt.presentation.filter.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.systemBarsPadding
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.ui.theme.Size_2X_Large
import com.rsicarelli.homehunt.ui.theme.rally_green_500

@Composable
fun SeeResultsButton(
    onClick: () -> Unit,
    previewResultCount: Int?,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .systemBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        val hasResults = previewResultCount != null
        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(Size_2X_Large),
            shape = MaterialTheme.shapes.large,
            enabled = hasResults,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = rally_green_500,
                contentColor = MaterialTheme.colors.background
            ),
            onClick = onClick
        )
        {
            val resources = LocalContext.current.resources

            val text = previewResultCount?.let {
                resources.getQuantityString(
                    R.plurals.see_results_plurals, it, it
                )
            } ?: stringResource(id = R.string.calculating_results)

            Text(
                text = text,
                style = MaterialTheme.typography.button.copy(fontSize = 16.sp)
            )
        }
    }
}