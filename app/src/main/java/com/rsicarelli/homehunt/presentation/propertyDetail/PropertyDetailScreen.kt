package com.rsicarelli.homehunt.presentation.propertyDetail

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.Space
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import coil.annotation.ExperimentalCoilApi
import coil.compose.LocalImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.core.util.toCurrency
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.presentation.components.*
import com.rsicarelli.homehunt.presentation.photoGallery.components.ZoomableImage
import com.rsicarelli.homehunt.presentation.propertyDetail.components.FullHeightBottomSheet
import com.rsicarelli.homehunt.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun PropertyDetailScreen(
    scaffoldDelegate: ScaffoldDelegate,
    viewModel: PropertyDetailViewModel = hiltViewModel()
) {
    PropertyDetailContent(
        scaffoldDelegate = scaffoldDelegate,
        state = viewModel.state.value,
        events = viewModel::onEvent
    )
}

@OptIn(ExperimentalCoilApi::class, ExperimentalMaterialApi::class)
@Composable
private fun PropertyDetailContent(
    scaffoldDelegate: ScaffoldDelegate,
    state: PropertyDetailState,
    events: (PropertyDetailEvents) -> Unit
) {
    state.property?.let { property ->
        Box(modifier = Modifier.fillMaxSize()) {
            PropertyDetail(
                property = property,
                scaffoldDelegate = scaffoldDelegate,
                events = events,
            )
            PropertyTopBar(
                isFavourited = property.isFavourited,
                onFavouriteClick = {
                    events(
                        PropertyDetailEvents.ToggleFavourite(
                            referenceId = property.reference,
                            isFavourited = !property.isFavourited
                        )
                    )
                }
            )

            GalleryBottomSheet(
                photosGalleryUrls = property.photoGalleryUrls,
                isEnabled = state.openGallery,
                onCollapsed = {
                    events(PropertyDetailEvents.CloseGallery)
                }
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
    scaffoldDelegate: ScaffoldDelegate,
    events: (PropertyDetailEvents) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            val hasVideo = property.videoUrl != null && property.videoUrl.isNotEmpty()

            GalleryCarousel(
                photoGallery = property.photoGalleryUrls,
                hasVideo = hasVideo,
                onOpenGallery = {
                    events(PropertyDetailEvents.OpenGallery)
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
            Tag(text = word)
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
@OptIn(
    ExperimentalPagerApi::class, ExperimentalCoilApi::class,
    androidx.compose.animation.ExperimentalAnimationApi::class
)
@Composable
private fun GalleryCarousel(
    photoGallery: List<String>,
    hasVideo: Boolean,
    onOpenGallery: () -> Unit,
    onPlayVideo: () -> Unit,
) {
    val state: PagerState = rememberPagerState()

    Box(contentAlignment = Alignment.BottomEnd) {
        HorizontalPager(count = photoGallery.size, state = state) { page ->
            Box(
                modifier = Modifier.clickable { onOpenGallery() }
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(256.dp),
                    painter = rememberImagePainter(
                        data = photoGallery[page],
                        builder = {
                            crossfade(true)
                        }
                    ),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Row(
            modifier = Modifier
                .padding(SpaceSmall)
        ) {
            AnimatedVisibility(
                visible = state.currentPage == 0 && hasVideo,
                enter = fadeIn(
                    initialAlpha = 0.4f
                ),
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 250)
                )
            ) {
                Row(
                    modifier = Modifier
                        .height(36.dp)
                        .background(
                            color = MaterialTheme.colors.background.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(
                            start = SpaceSmall,
                            end = SpaceMedium
                        )
                        .clickable { onPlayVideo() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_round_play),
                        contentDescription = stringResource(id = R.string.video_available)
                    )
                    Spacer(Modifier.width(SpaceSmallest))
                    Text(
                        text = stringResource(id = R.string.video_available),
                        style = MaterialTheme.typography.button,
                    )
                }
            }
            Spacer(modifier = Modifier.width(SpaceSmall))
            PagerIndicator(currentPage = state.currentPage, totalItems = photoGallery.size)
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
                    .clickable { onMapClick() },
                lat = lat,
                lng = lng,
                isLiteMode = true
            )
        }

    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagerApi::class)
@Composable
fun GalleryBottomSheet(
    photosGalleryUrls: List<String>,
    isEnabled: Boolean,
    onCollapsed: () -> Unit
) {
    if (!isEnabled) return

    FullHeightBottomSheet(
        isEnabled = isEnabled,
        onCollapsed = onCollapsed
    ) {
        val imageLoader = LocalImageLoader.current
        val coroutinesScope = rememberCoroutineScope()
        val pagerState = rememberPagerState()
        Box(contentAlignment = Alignment.BottomCenter) {
            HorizontalPager(
                count = photosGalleryUrls.size,
                state = pagerState
            ) { page ->
                var drawable: Drawable? by remember { mutableStateOf(null) }

                val request = ImageRequest.Builder(LocalContext.current)
                    .data(photosGalleryUrls[page])
                    .build()

                LaunchedEffect(key1 = "imageLoader", block = {
                    coroutinesScope.launch {
                        drawable = imageLoader.execute(request).drawable
                    }
                })

                drawable?.let {
                    ZoomableImage(
                        drawable = it,
                        resetZoom = pagerState.currentPage != page
                    )
                }
            }
        }
    }
}

@Composable
fun PagerIndicator(
    currentPage: Int,
    totalItems: Int
) {
    Box(
        modifier = Modifier
            .height(36.dp)
            .background(
                color = MaterialTheme.colors.background.copy(alpha = 0.8f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(
                start = SpaceMedium,
                end = SpaceMedium
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            style = MaterialTheme.typography.button,
            text = "${currentPage + 1} of $totalItems"
        )
    }
}