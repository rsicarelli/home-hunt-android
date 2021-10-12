package com.rsicarelli.homehunt.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import coil.annotation.ExperimentalCoilApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.util.toCurrency
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.presentation.components.FavouritableIconButton
import com.rsicarelli.homehunt.presentation.components.GalleryCarousel
import com.rsicarelli.homehunt.presentation.components.IconText
import com.rsicarelli.homehunt.ui.theme.ElevationSize
import com.rsicarelli.homehunt.ui.theme.Size_X_Small
import com.rsicarelli.homehunt.ui.theme.Size_Small
import com.rsicarelli.homehunt.ui.theme.Size_Regular

@OptIn(ExperimentalCoilApi::class, ExperimentalPagerApi::class)
@Composable
fun PropertyListItem(
    property: Property,
    onSelectProperty: (Property) -> Unit,
    onFavouriteClick: () -> Unit,
    extraContent: @Composable RowScope.(Property) -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = Size_Regular,
                bottom = Size_Small
            )
            .clip(MaterialTheme.shapes.large)
            .clickable {
                onSelectProperty(property)
            },
        color = MaterialTheme.colors.surface,
        elevation = ElevationSize
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                val pagerState = rememberPagerState()
                GalleryCarousel(
                    state = pagerState,
                    photoGallery = property.photoGalleryUrls,
                    onOpenGallery = { onSelectProperty(property) },
                )
                Row {
                    extraContent(property)
                    Box(
                        modifier = Modifier.padding(Size_Small)
                    ) {
                        FavouritableIconButton(
                            onClick = onFavouriteClick,
                            isFavourited = property.isFavourited
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(Size_Small))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = Size_Regular, end = Size_Regular),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "${property.price.toCurrency()}",
                    style = MaterialTheme.typography.h5,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(Size_Small))
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(top = Size_X_Small, start = Size_X_Small),
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
                        bottom = Size_Regular,
                        start = Size_Regular,
                        end = Size_Regular,
                        top = Size_X_Small
                    )
            ) {
                IconText(
                    text = "${property.dormCount}",
                    leadingIcon = R.drawable.ic_round_double_bed
                )
                Spacer(modifier = Modifier.width(Size_Regular))
                IconText(
                    text = "${property.bathCount}",
                    leadingIcon = R.drawable.ic_round_shower
                )
                Spacer(modifier = Modifier.width(Size_Regular))
                IconText(
                    text = "${property.surface} m²",
                    leadingIcon = R.drawable.ic_round_ruler
                )
            }

        }
    }

}