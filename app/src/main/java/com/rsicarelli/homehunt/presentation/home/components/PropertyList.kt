package com.rsicarelli.homehunt.presentation.home.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.insets.statusBarsPadding
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.ui.navigation.Screen
import com.rsicarelli.homehunt.ui.theme.*
import utils.Fixtures

@Composable
fun PropertyList(
    properties: List<Property>,
    scrollState: LazyListState = rememberLazyListState(),
    onNavigate: (String) -> Unit,
    onToggleFavourite: (Property) -> Unit,
    @StringRes headerPrefixRes: Int,
) {
    if (properties.isEmpty()) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
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
                        property = property,
                        onSelectProperty = { onNavigate("${Screen.PropertyDetail.route}/${property.reference}") },
                        onFavouriteClick = { onToggleFavourite(property) }
                    )
                }
                item { Spacer(modifier = Modifier.height(Size_Small)) }
            }
        }

        ResultsHeader(scrollState.isScrollInProgress, properties.size, headerPrefixRes)
    }
}

@Composable
@Preview
private fun PropertyListPreview() {
    HomeHuntTheme(isPreview = true) {
        PropertyList(
            properties = Fixtures.aListOfProperty,
            onNavigate = {},
            onToggleFavourite = {},
            headerPrefixRes = R.string.results
        )
    }
}