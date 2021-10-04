package com.rsicarelli.homehunt.presentation.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.core.model.UiEvent

@Composable
fun SplashScreen(
    scaffoldDelegate: ScaffoldDelegate,
    state: SplashState,
    events: (SplashEvents) -> Unit
) {
    when (state.uiEvent) {
        is UiEvent.MessageToUser -> scaffoldDelegate.showMessageToUser(state.uiEvent.uiText)
        is UiEvent.Navigate -> scaffoldDelegate.navigateSingleTop(state.uiEvent)
        UiEvent.NavigateUp -> scaffoldDelegate.navigateUp()
    }

    val scale = remember {
        Animatable(0f)
    }
    val overshootInterpolator = remember {
        OvershootInterpolator(2f)
    }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.5f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    overshootInterpolator.getInterpolation(it)
                }
            )
        )
        events(SplashEvents.AnimationEnded)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Logo",
            modifier = Modifier.scale(scale.value)
        )
    }
}