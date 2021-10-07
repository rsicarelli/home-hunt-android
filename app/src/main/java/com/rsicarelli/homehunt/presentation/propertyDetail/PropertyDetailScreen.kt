package com.rsicarelli.homehunt.presentation.propertyDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.presentation.login.LoginEvents
import com.rsicarelli.homehunt.presentation.login.LoginState

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
                    GalleryCarousel(property.photoGalleryUrls, imageLoader) {
                        scaffoldDelegate.launchPhotoDetailsGallery(property)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class, ExperimentalCoilApi::class)
@Composable
private fun GalleryCarousel(
    photoGallery: List<String>,
    imageLoader: ImageLoader,
    onClick: () -> Unit,
) {
// Display 10 items
    HorizontalPager(count = photoGallery.size) { page ->
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

