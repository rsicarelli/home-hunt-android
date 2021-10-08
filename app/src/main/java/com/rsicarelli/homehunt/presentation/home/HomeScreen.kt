package com.rsicarelli.homehunt.presentation.home

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.FloatingActionButtonDefaults.elevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.core.util.toCurrency
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.presentation.components.CircularIndeterminateProgressBar
import com.rsicarelli.homehunt.presentation.components.IconText
import com.rsicarelli.homehunt.presentation.components.OnLifecycleEvent
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.theme.SpaceMedium
import com.rsicarelli.homehunt.ui.theme.SpaceSmall
import com.rsicarelli.homehunt.ui.theme.SpaceSmallest
import com.rsicarelli.homehunt.ui.theme.rally_blue_700

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    scaffoldDelegate: ScaffoldDelegate,
    viewModel: HomeViewModel = hiltViewModel(),
    imageLoader: ImageLoader
) {

    HomeContent(viewModel::onEvent, viewModel.state.value, imageLoader, scaffoldDelegate)

}

@Composable
private fun HomeContent(
    events: (HomeEvents) -> Unit,
    state: HomeState,
    imageLoader: ImageLoader,
    scaffoldDelegate: ScaffoldDelegate
) {
    OnLifecycleEvent { event ->
        events(HomeEvents.LifecycleEvent(event))
    }

    EmptyProperties(state.emptyResults)

    PropertyList(properties = state.properties, imageLoader = imageLoader, scaffoldDelegate)

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

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun PropertyList(
    properties: List<Property>,
    imageLoader: ImageLoader,
    scaffoldDelegate: ScaffoldDelegate
) {
    if (properties.isEmpty()) return

    val scrollState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(SpaceMedium)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            AnimatedVisibility(visible = properties.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = scrollState
                ) {
                    stickyHeader { Spacer(modifier = Modifier.height(50.dp)) }
                    items(properties) { property ->
                        PropertyListItem(
                            property = property,
                            onSelectProperty = { scaffoldDelegate.navigate("${Screen.PropertyDetail.route}/${property.reference}") },
                            imageLoader = imageLoader,
                        )
                    }
                }
            }

            FilterFab(scrollState = scrollState) {
                scaffoldDelegate.navigate(UiEvent.Navigate(Screen.Filter.route))
            }
        }

        ResultsHeader(scrollState, properties.size)

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ResultsHeader(
    scrollState: LazyListState,
    resultCount: Int
) {
    val density = LocalDensity.current

    AnimatedVisibility(
        visible = !scrollState.isScrollInProgress,
        enter =
        slideInVertically(initialOffsetY = { with(density) { -50.dp.roundToPx() } })
                + expandVertically(expandFrom = Alignment.Top)
                + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp + SpaceSmall),
            color = MaterialTheme.colors.background
        ) {
            Text(
                modifier = Modifier.padding(bottom = SpaceSmall),
                text = "$resultCount ${stringResource(id = R.string.results)}",
                style = MaterialTheme.typography.h4
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun FilterFab(scrollState: LazyListState, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = !scrollState.isScrollInProgress,
        enter = slideInVertically() + expandVertically(
            expandFrom = Alignment.Bottom
        ) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically() + shrinkVertically() + fadeOut()
    ) {
        Box(modifier = Modifier.padding(bottom = SpaceSmall)) {
            FloatingActionButton(
                onClick = onClick,
                elevation = elevation(10.dp),
                backgroundColor = rally_blue_700
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_filter),
                    contentDescription = "Filter"
                )
            }
        }

    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun PropertyListItem(
    property: Property,
    onSelectProperty: (Property) -> Unit,
    imageLoader: ImageLoader
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = SpaceMedium,
                bottom = SpaceSmall
            )
            .clip(RoundedCornerShape(10.dp))
            .clickable {
                onSelectProperty(property)
            },
        color = MaterialTheme.colors.surface,
        elevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                    painter = painter,
                    contentDescription = property.title,
                    contentScale = ContentScale.FillWidth,
                )
            }
            Spacer(modifier = Modifier.height(SpaceSmall))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = SpaceMedium, end = SpaceMedium),
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "${property.price.toCurrency()}",
                    style = MaterialTheme.typography.h5,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.width(SpaceSmall))
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(top = SpaceSmallest, start = SpaceSmallest),
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
                        bottom = SpaceMedium,
                        start = SpaceMedium,
                        end = SpaceMedium,
                        top = SpaceSmallest
                    )
            ) {
                IconText(
                    text = "${property.dormCount}",
                    leadingIcon = R.drawable.ic_round_double_bed
                )
                Spacer(modifier = Modifier.width(SpaceMedium))
                IconText(
                    text = "${property.bathCount}",
                    leadingIcon = R.drawable.ic_round_shower
                )
                Spacer(modifier = Modifier.width(SpaceMedium))
                IconText(
                    text = "${property.surface}",
                    leadingIcon = R.drawable.ic_round_ruler
                )
            }

        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth(.8f) // fill 80% of remaining width
//                    .padding(start = 12.dp)
//            ) {
//                Text(
//                    modifier = Modifier
//                        .padding(bottom = 4.dp),
//                    text = property.title,
//                    style = MaterialTheme.typography.h4,
//                    maxLines = 1,
//                    overflow = TextOverflow.Ellipsis
//                )
//                Text(
//                    text = property.location,
//                    style = MaterialTheme.typography.subtitle1,
//                )
//            }
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth() // Fill the rest of the width (100% - 80% = 20%)
//                    .padding(end = 12.dp),
//                horizontalAlignment = Alignment.End
//            ) {
////
////                Text(
////                    text = "${proWR}%",
////                    style = MaterialTheme.typography.caption,
////                    color = if (proWR > 50) Color(0xFF009a34) else MaterialTheme.colors.error,
////                )
//            }
//        }
    }

}