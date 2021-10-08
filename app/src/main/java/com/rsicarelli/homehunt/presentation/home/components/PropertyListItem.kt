package com.rsicarelli.homehunt.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.util.toCurrency
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.presentation.components.FavouritableIconButton
import com.rsicarelli.homehunt.presentation.components.IconText
import com.rsicarelli.homehunt.ui.theme.SpaceMedium
import com.rsicarelli.homehunt.ui.theme.SpaceSmall
import com.rsicarelli.homehunt.ui.theme.SpaceSmallest

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PropertyListItem(
    property: Property,
    onSelectProperty: (Property) -> Unit,
    onFavouriteClick: () -> Unit,
    imageLoader: ImageLoader
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = SpaceMedium,
                bottom = SpaceSmall
            )
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onSelectProperty(property)
            },
        color = MaterialTheme.colors.surface,
        elevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                val painter = rememberImagePainter(
                    property.photoGalleryUrls.first(),
                    imageLoader = imageLoader,
                    builder = {
                        placeholder(if (isSystemInDarkTheme()) R.drawable.black_background else R.drawable.white_background)
                    }
                )
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                    painter = painter,
                    contentDescription = property.title,
                    contentScale = ContentScale.FillWidth,
                )
                Box(
                    modifier = Modifier.padding(SpaceSmall)
                ) {
                    FavouritableIconButton(
                        onFavouriteClick = onFavouriteClick,
                        isFavourited = property.isFavourited
                    )
                }
            }
            Spacer(modifier = Modifier.height(SpaceSmall))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = SpaceMedium, end = SpaceMedium),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "${property.price.toCurrency()}",
                    style = MaterialTheme.typography.h5,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(SpaceSmall))
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(top = SpaceSmallest, start = SpaceSmallest),
                    text = property.location,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.primaryVariant
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = SpaceMedium,
                        start = SpaceMedium,
                        end = SpaceMedium,
                        top = SpaceSmallest
                    )
            ) {
                IconText(
                    text = "${property.dormCount}",
                    leadingIcon = R.drawable.ic_round_double_bed
                )
                Spacer(modifier = Modifier.width(SpaceMedium))
                IconText(
                    text = "${property.bathCount}",
                    leadingIcon = R.drawable.ic_round_shower
                )
                Spacer(modifier = Modifier.width(SpaceMedium))
                IconText(
                    text = "${property.surface}",
                    leadingIcon = R.drawable.ic_round_ruler
                )
            }

        }
    }

}