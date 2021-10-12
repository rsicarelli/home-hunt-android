package com.rsicarelli.homehunt.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.ui.theme.Size_Regular

@Composable
fun BackButton(scaffoldDelegate: ScaffoldDelegate) {
    IconButton(
        modifier = Modifier.padding(top = Size_Regular),
        onClick = { scaffoldDelegate.navigateUp() }) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = stringResource(id = R.string.go_back)
        )
    }
}