package com.rsicarelli.homehunt.presentation.filter.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.ui.theme.Size_Small

@Composable
fun VisibilitySelector(
    isChecked: Boolean,
    onChange: (Boolean) -> Unit
) {
    Spacer(modifier = Modifier.height(Size_Small))

    CheckboxItem(
        title = stringResource(id = R.string.show_seen),
        isChecked = isChecked,
        onCheckedChange = onChange
    )
}