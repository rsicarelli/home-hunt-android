package com.rsicarelli.homehunt.presentation.propertyDetail

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.core.util.toCurrency
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.presentation.components.*
import com.rsicarelli.homehunt.ui.theme.*

@Composable
fun PropertyDetailScreen(
    imageLoader: ImageLoader,
    scaffoldDelegate: ScaffoldDelegate,
    viewModel: PropertyDetailViewModel = hiltViewModel()
) {
    PropertyDetailContent(imageLoader, scaffoldDelegate, viewModel.state.value, viewModel::onEvent)
}

@OptIn(ExperimentalCoilApi::class, ExperimentalMaterialApi::class)
@Composable
private fun PropertyDetailContent(
    imageLoader: ImageLoader,
    scaffoldDelegate: ScaffoldDelegate,
    state: PropertyDetailState,
    events: (PropertyDetailEvents) -> Unit
) {
    state.property?.let { property ->
        Box(modifier = Modifier.fillMaxSize()) {
            PropertyDetail(property, imageLoader, scaffoldDelegate)
            PropertyTopBar(
                onFavouriteClick = {
                    events(
                        PropertyDetailEvents.ToggleFavourite(
                            property.reference,
                            !property.isFavourited
                        )
                    )
                },
                property.isFavourited
            )
        }
    }
}

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PropertyDetail(
    property: Property,
    imageLoader: ImageLoader,
    scaffoldDelegate: ScaffoldDelegate
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            GalleryCarousel(
                photoGallery = property.photoGalleryUrls,
                imageLoader = imageLoader,
                hasVideo = property.videoUrl != null && property.videoUrl.isNotEmpty(),
                onOpenGallery = {
                    scaffoldDelegate.launchPhotoDetailsGallery(property)
                }, onPlayVideo = {
                    scaffoldDelegate.launchVideoPlayer(property.videoUrl!!)
                }
            )
        }
        item { PropertyHeader(property) }
        item { PropertyFeatures(property.characteristics) }
        item {
            PropertyMap(
                lat = property.lat,
                lng = property.lng,
                onMapClick = {
                    //TODO
                }
            )
        }
        item {
            PropertyDetails(
                titleRes = R.string.about_this_property,
                content = property.fullDescription
            )
        }
        item {
            PropertyDetails(
                titleRes = R.string.location_description,
                content = property.locationDescription
            )
        }
        item {
            PropertyFooter(
                property.reference,
                property.propertyUrl,
                property.pdfUrl
            )
        }
    }
}

@Composable
fun PropertyFeatures(characteristics: List<String>) {
    ChipVerticalGrid(
        spacing = 7.dp,
        modifier = Modifier
            .padding(start = SpaceMedium, end = SpaceMedium, bottom = SpaceMedium)
    ) {
        characteristics.forEach { word ->
            Surface(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp)),
                color = MaterialTheme.colors.surface,
                elevation = 8.dp
            ) {
                Text(
                    word,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier
                        .background(Color.Unspecified)
                        .padding(
                            start = SpaceSmall,
                            end = SpaceSmall,
                            top = SpaceSmallest,
                            bottom = SpaceSmallest
                        )
                )
            }
        }
    }
}

@Composable
fun PropertyFooter(reference: String, propertyUrl: String, pdfUrl: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SpaceMedium)
    ) {

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(
                    MaterialTheme.colors.primary.copy(alpha = 0.3f),
                    RoundedCornerShape(40.dp)
                )
        )

        Spacer(modifier = Modifier.height(SpaceSmall))

        SelectionContainer {
            Text(reference, style = MaterialTheme.typography.overline.copy(fontSize = 14.sp))
        }

        Spacer(modifier = Modifier.height(SpaceSmall))

        CustomClickableText(
            propertyUrl = propertyUrl,
            placeholder = R.string.see_on_web
        )

        pdfUrl?.let {
            CustomClickableText(
                propertyUrl = it,
                placeholder = R.string.see_pdf
            )
        }
    }
}

@Composable
private fun CustomClickableText(
    propertyUrl: String,
    @StringRes placeholder: Int = R.string.see_on_web,
) {
    val annotatedText = buildAnnotatedString {
        pushStringAnnotation(
            tag = "URL",
            annotation = propertyUrl
        )
        withStyle(
            style = MaterialTheme.typography.overline.copy(
                fontSize = 14.sp,
                color = rally_blue_100
            ).toSpanStyle()
        ) {
            append(stringResource(id = placeholder))
        }
        pop()
    }

    val context = LocalContext.current
    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
                .firstOrNull()?.let { annotation ->
                    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                    context.startActivity(webIntent)
                }
        }
    )
}

@Composable
fun PropertyLocationDetails(property: Property) {
    property.fullDescription?.let {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(SpaceMedium)
        ) {
            Text(
                text = stringResource(id = R.string.about_this_property),
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(SpaceSmall))
            ExpandableText(text = property.fullDescription)
        }
    }
}

@Composable
fun PropertyDetails(
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

@Composable
fun PropertyHeader(
    property: Property
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SpaceMedium)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (specs, price, title, location) = createRefs()
            val barrier = createEndBarrier(location, specs)

            Text(
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top)
                },
                text = property.title,
                style = MaterialTheme.typography.h6.copy(lineHeight = 24.sp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            IconText(
                modifier = Modifier.constrainAs(location) {
                    top.linkTo(title.bottom, SpaceSmallest)
                },
                text = property.location,
                textStyle = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.primaryVariant),
                leadingIcon = R.drawable.ic_round_location,
            )

            Row(modifier = Modifier
                .constrainAs(specs) {
                    top.linkTo(location.bottom, SpaceSmallest)
                }) {
                IconText(
                    text = property.dormCount.toString(),
                    textStyle = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.primaryVariant),
                    leadingIcon = R.drawable.ic_round_double_bed
                )
                Spacer(modifier = Modifier.width(SpaceSmall))
                IconText(
                    text = property.bathCount.toString(),
                    textStyle = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.primaryVariant),
                    leadingIcon = R.drawable.ic_round_shower
                )
                Spacer(modifier = Modifier.width(SpaceSmall))
                IconText(
                    text = "${property.surface} m²",
                    textStyle = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.primaryVariant),
                    leadingIcon = R.drawable.ic_round_ruler
                )
            }

            Text(
                modifier = Modifier.constrainAs(price) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, margin = (-SpaceMedium))
                    start.linkTo(barrier, SpaceMedium)
                },
                text = "${property.price.toCurrency()}",
                style = MaterialTheme.typography.h4.copy(color = rally_blue),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}

@ExperimentalMaterialApi
@OptIn(ExperimentalPagerApi::class, ExperimentalCoilApi::class)
@Composable
private fun GalleryCarousel(
    photoGallery: List<String>,
    imageLoader: ImageLoader,
    hasVideo: Boolean,
    onOpenGallery: () -> Unit,
    onPlayVideo: () -> Unit,
) {
    HorizontalPager(photoGallery.size) { page ->
        Box(
            modifier = Modifier.clickable { onOpenGallery() },
            contentAlignment = Alignment.BottomEnd
        ) {
            val painter = rememberImagePainter(
                photoGallery[page],
                imageLoader = imageLoader,
                builder = {
                    placeholder(if (isSystemInDarkTheme()) R.drawable.black_background else R.drawable.white_background)
                }
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp),
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            Row(
                modifier = Modifier
                    .padding(SpaceSmall)
            ) {
                if (page == 0 && hasVideo) {
                    OutlinedButton(
                        modifier = Modifier.height(36.dp),
                        onClick = onPlayVideo,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = MaterialTheme.colors.background.copy(alpha = 0.8f),
                        ),
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_round_play),
                            contentDescription = stringResource(id = R.string.video_available)
                        )
                        Text(
                            text = stringResource(id = R.string.video_available)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(SpaceSmall))
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .background(
                            MaterialTheme.colors.background.copy(alpha = 0.8f),
                            RoundedCornerShape(10.dp)
                        )
                        .padding(start = SpaceMedium, end = SpaceMedium),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        style = MaterialTheme.typography.button,
                        text = "${currentPage + 1} of ${photoGallery.size}"
                    )
                }

            }

        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
private fun PropertyMap(
    lat: Double?,
    lng: Double?,
    onMapClick: () -> Unit
) {
    if (lat == null || lng == null || lat == 0.0 || lng == 0.0) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = SpaceMedium, end = SpaceMedium)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            MapView(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onMapClick() }, lat = lat, lng = lng, isLiteMode = true
            )
        }

    }
}

