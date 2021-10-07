package com.rsicarelli.homehunt.presentation.propertyDetail

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
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
import com.rsicarelli.homehunt.presentation.components.ExpandableText
import com.rsicarelli.homehunt.presentation.components.IconText
import com.rsicarelli.homehunt.ui.theme.*


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

@OptIn(
    ExperimentalPagerApi::class, ExperimentalCoilApi::class,
    ExperimentalMaterialApi::class
)
@Composable
private fun PropertyDetailContent(
    imageLoader: ImageLoader,
    scaffoldDelegate: ScaffoldDelegate,
    state: PropertyDetailState,
    events: (PropertyDetailEvents) -> Unit
) {
    state.property?.let { property ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                GalleryCarousel(
                    photoGallery = property.photoGalleryUrls,
                    imageLoader = imageLoader,
                    hasVideo = property.videoUrl != null,
                    onOpenGallery = {
                        scaffoldDelegate.launchPhotoDetailsGallery(property)
                    }, onPlayVideo = {
                        scaffoldDelegate.launchVideoPlayer(property.videoUrl!!)
                    }
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background)
                        .padding(SpaceMedium)
                ) {
                    PropertyHeader(property) {
                        scaffoldDelegate.launchVideoPlayer(it)
                    }
                    PropertyDetails(property)
                }
            }
        }
    }
}

@Composable
fun PropertyDetails(property: Property) {
    property.fullDescription?.let {
        Column(Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.height(SpaceMedium))
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
fun PropertyHeader(
    property: Property,
    onPlayVideoClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
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
                    top.linkTo(title.bottom, margin = (-SpaceSmallest))
                    end.linkTo(parent.end)
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
@OptIn(ExperimentalPagerApi::class, coil.annotation.ExperimentalCoilApi::class)
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
                    .height(200.dp),
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
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

