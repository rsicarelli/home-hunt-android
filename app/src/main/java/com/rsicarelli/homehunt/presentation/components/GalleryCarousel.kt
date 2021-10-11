package com.rsicarelli.homehunt.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class, androidx.compose.animation.ExperimentalAnimationApi::class)
@Composable
fun GalleryCarousel(
    state: PagerState = rememberPagerState(),
    photoGallery: List<String>,
    onOpenGallery: () -> Unit,
    imageSize: Dp = 200.dp,
    footer: @Composable BoxScope.(Int) -> Unit = {}
) {
    Box(contentAlignment = Alignment.BottomEnd) {
        HorizontalPager(count = photoGallery.size, state = state) { page ->
            Box(
                modifier = Modifier.clickable { onOpenGallery() }
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageSize),
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
        footer(state.currentPage)
    }
}