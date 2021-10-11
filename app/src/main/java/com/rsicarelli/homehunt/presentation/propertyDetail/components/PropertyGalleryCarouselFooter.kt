package com.rsicarelli.homehunt.presentation.propertyDetail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.ui.theme.SpaceMedium
import com.rsicarelli.homehunt.ui.theme.SpaceSmall
import com.rsicarelli.homehunt.ui.theme.SpaceSmallest

@ExperimentalAnimationApi
@Composable
fun PropertyGalleryCarouselFooter(
    hasVideo: Boolean,
    currentPage: Int,
    onPlayVideo: () -> Unit,
    gallerySize: Int
) {
    Row(
        modifier = Modifier
            .padding(SpaceSmall)
    ) {
        AnimatedVisibility(
            visible = currentPage == 0 && hasVideo,
            enter = fadeIn(
                initialAlpha = 0.4f
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 250)
            )
        ) {
            Row(
                modifier = Modifier
                    .height(36.dp)
                    .background(
                        color = MaterialTheme.colors.background.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(
                        start = SpaceSmall,
                        end = SpaceMedium
                    )
                    .clickable { onPlayVideo() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_round_play),
                    contentDescription = stringResource(id = R.string.video_available)
                )
                Spacer(Modifier.width(SpaceSmallest))
                Text(
                    text = stringResource(id = R.string.video_available),
                    style = MaterialTheme.typography.button,
                )
            }
        }
        Spacer(modifier = Modifier.width(SpaceSmall))
        PagerIndicator(currentPage = currentPage, totalItems = gallerySize)
    }
}