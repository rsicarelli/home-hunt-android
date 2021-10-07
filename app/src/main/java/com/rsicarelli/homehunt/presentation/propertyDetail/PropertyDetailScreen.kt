package com.rsicarelli.homehunt.presentation.propertyDetail

import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.core.util.toCurrency
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.presentation.components.IconText
import com.rsicarelli.homehunt.ui.theme.SpaceMedium
import com.rsicarelli.homehunt.ui.theme.SpaceSmall
import com.rsicarelli.homehunt.ui.theme.SpaceSmallest
import com.rsicarelli.homehunt.ui.theme.rally_blue


val defaultProperty = Property(
    reference = "a reference",
    price = 100.0,
    title = "a title",
    location = "a location",
    surface = 30,
    dormCount = 2,
    description = "a description",
    bathCount = 2,
    avatarUrl = "https://someimage.com",
    tag = "EMPTY",
    propertyUrl = "https://someurl.com",
    videoUrl = "https://somevideo.com",
    fullDescription = "a full description",
    locationDescription = "a location description",
    characteristics = listOf("foo", "bar"),
    photoGalleryUrls = listOf(
        "https://www.aproperties.es/media/properties/6713/6713_15904075335777.jpg",
        "https://www.aproperties.es/media/properties/31800/31800_1632323417065.jpg",
        "https://www.aproperties.es/media/properties/31800/31800_16323234170252.jpg",
        "https://www.aproperties.es/media/properties/31800/31800_16323234170444.jpg",
        "https://www.aproperties.es/media/properties/31800/31800_1632323417058.jpg",
        "https://www.aproperties.es/media/properties/31800/31800_16323234170475.jpg",
        "https://www.aproperties.es/media/properties/31800/31800_16323234170513.jpg",
        "https://www.aproperties.es/media/properties/31800/31800_1632323417032.jpg",
        "https://www.aproperties.es/media/properties/31800/31800_16323234170399.jpg",
        "https://www.aproperties.es/media/properties/31800/31800_16323234170618.jpg",
        "https://www.aproperties.es/media/properties/31800/31800_16323234170359.jpg",
        "https://www.aproperties.es/media/properties/31800/31800_16323234170541.jpg",
        "https://www.aproperties.es/media/properties/31800/31800_16323234170214.jpg",
    ),
    lat = 4.0,
    lng = 2.0,
    pdfUrl = "https://apdf.com",
    origin = "",
    viewedBy = emptyList(),
    favouriteBy = emptyList()
)

@Composable
fun PropertyDetailScreen(
    imageLoader: ImageLoader,
    scaffoldDelegate: ScaffoldDelegate,
    viewModel: PropertyDetailViewModel = hiltViewModel()
) {
    PropertyDetailContent(imageLoader, scaffoldDelegate, viewModel.state.value, viewModel::onEvent)
}

@OptIn(ExperimentalPagerApi::class, ExperimentalCoilApi::class)
@Composable
private fun PropertyDetailContent(
    imageLoader: ImageLoader,
    scaffoldDelegate: ScaffoldDelegate,
    state: PropertyDetailState,
    events: (PropertyDetailEvents) -> Unit
) {
    state.property?.let { property ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                ) {
                    val modifier = Modifier.padding(SpaceMedium)
                    GalleryCarousel(property.photoGalleryUrls, imageLoader) {
                        scaffoldDelegate.launchPhotoDetailsGallery(property)
                    }
                    Header(modifier, property) {
                        scaffoldDelegate.launchVideoPlayer(it)
                    }

                }
            }
        }
    }
}

@Composable
fun Header(
    modifier: Modifier,
    property: Property,
    onPlayVideoClick: (String) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = property.title,
            style = MaterialTheme.typography.h6.copy(lineHeight = 24.sp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(SpaceSmallest))
        IconText(
            text = property.location,
            textStyle = MaterialTheme.typography.subtitle2.copy(color = MaterialTheme.colors.primaryVariant),
            leadingIcon = R.drawable.ic_round_location
        )
        Spacer(modifier = Modifier.height(SpaceSmall))

        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (specs, price, video, watchSubtitle) = createRefs()

            Row(modifier = Modifier
                .constrainAs(specs) {
                    top.linkTo(parent.top)
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
                    top.linkTo(specs.bottom, SpaceSmallest)
                },
                text = "${property.price.toCurrency()}",
                style = MaterialTheme.typography.h5.copy(color = rally_blue),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            property.videoUrl?.let {
                Column(
                    modifier = Modifier
                        .constrainAs(video) {
                            end.linkTo(parent.end, margin = SpaceMedium)
                        }
                        .clickable {
                            onPlayVideoClick(property.videoUrl)
                        }
                        .width(90.dp)
                        .padding(SpaceSmallest),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .border(
                                BorderStroke(
                                    1.dp,
                                    MaterialTheme.colors.primaryVariant
                                ), RoundedCornerShape(16.dp)
                            )
                            .height(48.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_round_play),
                            contentDescription = "content description"
                        )
                    }
                    Spacer(modifier = Modifier.height(SpaceSmallest))
                    Text(
                        text = stringResource(id = R.string.video_available),
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }

    }
}

@OptIn(
    ExperimentalPagerApi::class, ExperimentalCoilApi::class,
    ExperimentalMaterialApi::class
)
@Composable
private fun GalleryCarousel(
    photoGallery: List<String>,
    imageLoader: ImageLoader,
    onClick: () -> Unit,
) {
    HorizontalPager(photoGallery.size) { page ->
        PhotoItem(onClick, photoGallery, page, imageLoader)
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalCoilApi::class)
@Composable
private fun PhotoItem(
    onClick: () -> Unit,
    photoGallery: List<String>,
    page: Int,
    imageLoader: ImageLoader
) {
    Surface(onClick = onClick) {
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
                .height(200.dp),
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )
    }
}

