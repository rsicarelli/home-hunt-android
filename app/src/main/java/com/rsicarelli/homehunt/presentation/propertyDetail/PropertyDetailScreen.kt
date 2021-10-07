package com.rsicarelli.homehunt.presentation.propertyDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.rsicarelli.homehunt.presentation.login.LoginEvents
import com.rsicarelli.homehunt.presentation.login.LoginState
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

@OptIn(ExperimentalPagerApi::class, ExperimentalCoilApi::class)
@Composable
private fun PropertyDetailContent(
    imageLoader: ImageLoader, scaffoldDelegate: ScaffoldDelegate,
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
                    Header(modifier, property)

                }
            }
        }
    }
}

@Composable
fun Header(
    modifier: Modifier,
    property: Property
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
        Row(modifier = Modifier.fillMaxWidth()) {
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
        Spacer(modifier = Modifier.height(SpaceSmallest))
        Text(
            text = "${property.price.toCurrency()}",
            style = MaterialTheme.typography.h5.copy(color = rally_blue),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(
    ExperimentalPagerApi::class, ExperimentalCoilApi::class,
    androidx.compose.material.ExperimentalMaterialApi::class
)
@Composable
private fun GalleryCarousel(
    photoGallery: List<String>,
    imageLoader: ImageLoader,
    onClick: () -> Unit,
) {
    HorizontalPager(count = photoGallery.size) { page ->
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
}

