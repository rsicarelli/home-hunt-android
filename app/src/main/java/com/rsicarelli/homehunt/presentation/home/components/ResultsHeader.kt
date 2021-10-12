package com.rsicarelli.homehunt.presentation.home.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.rsicarelli.homehunt.ui.theme.Size_4X_Large
import com.rsicarelli.homehunt.ui.theme.Size_Regular

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ResultsHeader(
    scrollState: LazyListState,
    resultCount: Int,
    @StringRes headerPrefixRes: Int
) {
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