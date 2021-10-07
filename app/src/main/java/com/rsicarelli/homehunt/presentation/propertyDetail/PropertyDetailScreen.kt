package com.rsicarelli.homehunt.presentation.propertyDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.presentation.propertyDetail.components.Pager

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
fun PropertyDetailScreen(imageLoader: ImageLoader) {
    PropertyDetailContent(imageLoader)
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun PropertyDetailContent(imageLoader: ImageLoader) {
    val pagerState = rememberPagerState()

    val (lastPage, setLastPage) = remember { mutableStateOf(0) }

    Pager(
        items = defaultProperty.photoGalleryUrls,
        modifier = Modifier
            .fillMaxSize()
        ,
        itemSpacing = 0.dp,
        contentFactory = { item ->
            Box(modifier = Modifier.fillMaxSize()){
                val painter = rememberImagePainter(
                    item,
                    imageLoader = imageLoader,
                    builder = {
                        placeholder(if (isSystemInDarkTheme()) com.rsicarelli.homehunt.R.drawable.black_background else com.rsicarelli.homehunt.R.drawable.white_background)
                    }
                )

                ZoomableImage2(
                    painter = painter,
                    isRotation = false,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

        }
    )

}


@Composable
fun ZoomableImage2(
    modifier: Modifier = Modifier,
    painter: Painter,
    maxScale: Float = .30f,
    minScale: Float = 3f,
    contentScale: ContentScale = ContentScale.Fit,
    isRotation: Boolean = false,
    isZoomable: Boolean = true
) {
    val scale = remember { mutableStateOf(1f) }
    val rotationState = remember { mutableStateOf(1f) }
    val offsetX = remember { mutableStateOf(1f) }
    val offsetY = remember { mutableStateOf(1f) }

    Box(
        modifier = Modifier
            .clip(RectangleShape)
            .background(Color.Transparent)
            .pointerInput(Unit) {
                if (isZoomable) {
                    forEachGesture {
                        awaitPointerEventScope {
                            awaitFirstDown()
                            do {
                                val event = awaitPointerEvent()
                                scale.value *= event.calculateZoom()
                                if (scale.value > 1) {
                                    val offset = event.calculatePan()
                                    offsetX.value += offset.x
                                    offsetY.value += offset.y
                                    rotationState.value += event.calculateRotation()
                                } else {
                                    scale.value = 1f
                                    offsetX.value = 1f
                                    offsetY.value = 1f
                                }
                            } while (event.changes.any { it.pressed })
                        }
                    }
                }
            }

    ) {
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = contentScale,
            modifier = modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    if (isZoomable) {
                        scaleX = maxOf(maxScale, minOf(minScale, scale.value))
                        scaleY = maxOf(maxScale, minOf(minScale, scale.value))
                        if (isRotation) {
                            rotationZ = rotationState.value
                        }
                        translationX = offsetX.value
                        translationY = offsetY.value
                    }
                }
        )
    }
}

/**
 * Creates a [PagerSnapState] that is remembered across compositions.
 */
@Composable
fun rememberPagerSnapState(): PagerSnapState {
    return remember {
        PagerSnapState()
    }
}

/**
 * This class maintains the state of the pager snap.
 */
class PagerSnapState {

    val isSwiping = mutableStateOf(false)

    val firstVisibleItemIndex = mutableStateOf(0)

    val offsetInfo = mutableStateOf(0)

    internal fun updateScrollToItemPosition(itemPos: LazyListItemInfo?) {
        if (itemPos != null) {
            this.offsetInfo.value = itemPos.offset
            this.firstVisibleItemIndex.value = itemPos.index
        }
    }

    internal suspend fun scrollItemToSnapPosition(listState: LazyListState, position: Int) {
        listState.animateScrollToItem(position)
    }
}

/**
 * [PagerSnapNestedScrollConnection] reacts to the scroll left to right and vice-versa.
 */
class PagerSnapNestedScrollConnection(
    private val state: PagerSnapState,
    private val listState: LazyListState,
    private val scrollTo: () -> Unit
) : NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset =
        when (source) {
            NestedScrollSource.Drag -> onScroll()
            else -> Offset.Zero
        }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset = when (source) {
        NestedScrollSource.Drag -> onScroll()
        else -> Offset.Zero
    }

    private fun onScroll(): Offset {
        state.isSwiping.value = true
        return Offset.Zero
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity = when {
        state.isSwiping.value -> {

            state.updateScrollToItemPosition(listState.layoutInfo.visibleItemsInfo.firstOrNull())

            scrollTo()

            Velocity.Zero
        }
        else -> {
            Velocity.Zero
        }
    }.also {
        state.isSwiping.value = false
    }

}
