package com.rsicarelli.homehunt.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleEventObserver
import coil.ImageLoader
import coil.compose.rememberImagePainter
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.domain.Property
import com.rsicarelli.homehunt.presentation.components.CircularIndeterminateProgressBar
import com.rsicarelli.homehunt.ui.theme.SpaceSmall

@Composable
fun HomeScreen(
    scaffoldDelegate: ScaffoldDelegate,
    state: HomeState,
    events: (HomeEvents) -> Unit,
    imageLoader: ImageLoader
) {

    OnLifecycleEvent(events)

    EmptyProperties(state.emptyResults)

    PropertyList(properties = state.properties, imageLoader = imageLoader)

    CircularIndeterminateProgressBar(state.progressBarState)
}

@Composable
fun EmptyProperties(emptyResults: Boolean) {
    if (!emptyResults) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        Image(
            painter = painterResource(R.drawable.empty_state),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Inside
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PropertyList(properties: List<Property>, imageLoader: ImageLoader) {
    if (properties.isEmpty()) return

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
        ) {
            Text(
                text = "${properties.size} ${stringResource(id = R.string.results)}",
                style = MaterialTheme.typography.h2
            )
            AnimatedVisibility(visible = properties.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = SpaceSmall)
                ) {
                    items(properties) { property ->
                        PropertyListItem(
                            property = property,
                            onSelectProperty = { },
                            imageLoader = imageLoader,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PropertyListItem(
    property: Property,
    onSelectProperty: (String) -> Unit,
    imageLoader: ImageLoader
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = SpaceSmall)
            .background(MaterialTheme.colors.surface)
            .clickable {
                onSelectProperty(property.reference)
            },
        elevation = SpaceSmall
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = rememberImagePainter(
                property.avatarUrl,
                imageLoader = imageLoader,
                builder = {
                    placeholder(if (isSystemInDarkTheme()) R.drawable.black_background else R.drawable.white_background)
                }
            )
            Image(
                modifier = Modifier
                    .width(120.dp)
                    .height(70.dp),
                painter = painter,
                contentDescription = property.title,
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(.8f) // fill 80% of remaining width
                    .padding(start = 12.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 4.dp),
                    text = property.title,
                    style = MaterialTheme.typography.h4,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = property.location,
                    style = MaterialTheme.typography.subtitle1,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth() // Fill the rest of the width (100% - 80% = 20%)
                    .padding(end = 12.dp),
                horizontalAlignment = Alignment.End
            ) {
//
//                Text(
//                    text = "${proWR}%",
//                    style = MaterialTheme.typography.caption,
//                    color = if (proWR > 50) Color(0xFF009a34) else MaterialTheme.colors.error,
//                )
            }
        }
    }

}

@Composable
fun OnLifecycleEvent(onEvent: (HomeEvents) -> Unit) {
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            onEvent(HomeEvents.LifecycleEvent(event))
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}