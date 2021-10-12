package com.rsicarelli.homehunt.presentation.propertyDetail.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.statusBarsPadding
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.presentation.components.FavouritableIconButton
import com.rsicarelli.homehunt.ui.theme.HomeHuntTheme
import com.rsicarelli.homehunt.ui.theme.IconSizeLarge
import com.rsicarelli.homehunt.ui.theme.Size_Small

@Composable
fun PropertyTopBar(
    onFavouriteClick: () -> Unit,
    onBackClicked: () -> Unit,
    isFavourited: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(Size_Small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onBackClicked) {
            Icon(
                modifier = Modifier.size(IconSizeLarge),
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = stringResource(
                    id = R.string.go_back
                )
            )
        }

        FavouritableIconButton(
            onFavouriteClick = onFavouriteClick,
            isFavourited = isFavourited
        )
    }
}

@Composable
@Preview
fun PropertyTopBarPreview() {
    HomeHuntTheme {
        PropertyTopBar(onFavouriteClick = {}, isFavourited = false, onBackClicked = {})
    }
}