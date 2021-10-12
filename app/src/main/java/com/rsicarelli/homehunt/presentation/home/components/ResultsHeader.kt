package com.rsicarelli.homehunt.presentation.home.components

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.ui.theme.HomeHuntTheme
import com.rsicarelli.homehunt.ui.theme.Size_4X_Large
import com.rsicarelli.homehunt.ui.theme.Size_Regular

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ResultsHeader(
    isScrollInProgress: Boolean,
    resultCount: Int,
    @StringRes headerPrefixRes: Int
) {
    AnimatedVisibility(
        visible = !isScrollInProgress,
        enter = expandVertically(expandFrom = Alignment.Top),
        exit = shrinkVertically()
    ) {
        Text(
            modifier = Modifier
                .padding(top = Size_Regular)
                .fillMaxWidth()
                .height(Size_4X_Large),
            text = "$resultCount ${stringResource(id = headerPrefixRes)}",
            style = MaterialTheme.typography.h4
        )
    }
}

@Composable
@Preview
private fun ResultsHeaderPreview() {
    HomeHuntTheme(isPreview = true) {
        ResultsHeader(
            isScrollInProgress = false,
            resultCount = 400,
            headerPrefixRes = R.string.results
        )
    }
}