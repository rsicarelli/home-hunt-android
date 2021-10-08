package com.rsicarelli.homehunt.presentation.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.rsicarelli.homehunt.R

@Composable
fun FavouritableIconButton(
    modifier: Modifier = Modifier,
    onFavouriteClick: () -> Unit,
    isFavourited: Boolean
) {
    IconButton(onClick = onFavouriteClick) {
        if (isFavourited) {
            Icon(
                modifier = modifier,
                painter = painterResource(id = R.drawable.ic_round_favorite),
                contentDescription = stringResource(
                    id = R.string.favourite
                )
            )
        } else {
            Icon(
                modifier = modifier,
                painter = painterResource(id = R.drawable.ic_round_favorite_border),
                contentDescription = stringResource(
                    id = R.string.unfavourite
                )
            )
        }
    }
}