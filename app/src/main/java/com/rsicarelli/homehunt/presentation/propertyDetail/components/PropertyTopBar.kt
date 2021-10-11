package com.rsicarelli.homehunt.presentation.propertyDetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsPadding
import com.rsicarelli.homehunt.presentation.components.FavouritableIconButton
import com.rsicarelli.homehunt.ui.theme.SpaceMedium

@Composable
fun PropertyTopBar(
    onFavouriteClick: () -> Unit,
    isFavourited: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(SpaceMedium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        FavouritableIconButton(
            onFavouriteClick = onFavouriteClick,
            isFavourited = isFavourited
        )
    }
}