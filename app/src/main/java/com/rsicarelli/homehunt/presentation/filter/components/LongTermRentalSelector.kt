package com.rsicarelli.homehunt.presentation.filter.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.ui.theme.Size_X_Small

@Composable
fun LongTermRentalSelector(
    isChecked: Boolean,
    onChange: (Boolean) -> Unit,
) {
    Spacer(modifier = Modifier.height(Size_X_Small))

    CheckboxItem(
        title = stringResource(id = R.string.show_longer_term_only),
        isChecked = isChecked,
        onCheckedChange = onChange
    )
}