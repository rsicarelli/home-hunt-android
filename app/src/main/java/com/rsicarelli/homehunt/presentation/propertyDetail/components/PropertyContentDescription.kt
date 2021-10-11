package com.rsicarelli.homehunt.presentation.propertyDetail.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rsicarelli.homehunt.presentation.components.ExpandableText
import com.rsicarelli.homehunt.ui.theme.SpaceMedium
import com.rsicarelli.homehunt.ui.theme.SpaceSmall

@Composable
fun PropertyContentDescription(
    @StringRes titleRes: Int,
    content: String?,
) {
    content.takeIf { it != null && it.isNotEmpty() }?.let { text ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(SpaceMedium)
        ) {
            Text(
                text = stringResource(id = titleRes),
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(SpaceSmall))
            ExpandableText(text)
        }
    }
}