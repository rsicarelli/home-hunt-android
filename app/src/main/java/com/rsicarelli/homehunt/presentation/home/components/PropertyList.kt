package com.rsicarelli.homehunt.presentation.home.components

import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.insets.systemBarsPadding
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.theme.SpaceMedium
import com.rsicarelli.homehunt.ui.theme.SpaceSmall
import com.rsicarelli.homehunt.ui.theme.rally_blue_700

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun PropertyList(
    properties: List<Property>,
    imageLoader: ImageLoader,
    scaffoldDelegate: ScaffoldDelegate,
    onToggleFavourite: (Property) -> Unit,
    showFab: Boolean,
    @StringRes headerPrefixRes: Int,
    extraContent: @Composable RowScope.(Property) -> Unit = {}
) {
    if (properties.isEmpty()) return

    val scrollState = rememberLazyListState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .systemBarsPadding()
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
                            extraContent = extraContent,
                            property = property,
                            onSelectProperty = { scaffoldDelegate.navigate("${Screen.PropertyDetail.route}/${property.reference}") },
                            imageLoader = imageLoader,
                            onFavouriteClick = { onToggleFavourite(property) }
                        )
                    }
                }
            }

            if (showFab) {
                FilterFab(scrollState = scrollState) {
                    scaffoldDelegate.navigate(UiEvent.Navigate(Screen.Filter.route))
                }
            }
        }

        ResultsHeader(scrollState, properties.size, headerPrefixRes)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ResultsHeader(
    scrollState: LazyListState,
    resultCount: Int,
    @StringRes headerPrefixRes: Int
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
                text = "$resultCount ${stringResource(id = headerPrefixRes)}",
                style = MaterialTheme.typography.h4
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun FilterFab(scrollState: LazyListState, onClick: () -> Unit) {
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
                elevation = FloatingActionButtonDefaults.elevation(10.dp),
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