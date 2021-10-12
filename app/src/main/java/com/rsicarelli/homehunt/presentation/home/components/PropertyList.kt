package com.rsicarelli.homehunt.presentation.home.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import com.google.accompanist.insets.systemBarsPadding
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.theme.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun PropertyList(
    scrollState: LazyListState = rememberLazyListState(),
    properties: List<Property>,
    scaffoldDelegate: ScaffoldDelegate,
    onToggleFavourite: (Property) -> Unit,
    @StringRes headerPrefixRes: Int,
    extraContent: @Composable RowScope.(Property) -> Unit = {}
) {
    if (properties.isEmpty()) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(
                start = Size_Regular,
                end = Size_Regular,
                bottom = Size_Regular
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = Size_Large),
            contentAlignment = Alignment.BottomEnd
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = scrollState
            ) {
                item { Spacer(modifier = Modifier.height(Size_3X_Large)) }
                items(properties) { property ->
                    PropertyListItem(
                        extraContent = extraContent,
                        property = property,
                        onSelectProperty = { scaffoldDelegate.navigate("${Screen.PropertyDetail.route}/${property.reference}") },
                        onFavouriteClick = { onToggleFavourite(property) }
                    )
                }
                item { Spacer(modifier = Modifier.height(Size_Small)) }
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
        enter = expandVertically(expandFrom = Alignment.Top),
        exit = shrinkVertically()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(Size_4X_Large),
            color = MaterialTheme.colors.background
        ) {
            Text(
                modifier = Modifier.padding(top = Size_Regular),
                text = "$resultCount ${stringResource(id = headerPrefixRes)}",
                style = MaterialTheme.typography.h4
            )
        }
    }
}