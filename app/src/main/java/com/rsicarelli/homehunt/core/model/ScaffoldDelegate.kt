package com.rsicarelli.homehunt.core.model

import android.content.Context
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.navigation.NavController
import com.rsicarelli.homehunt.core.util.asString
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.presentation.gallery.launchPhotoGalleryActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ScaffoldDelegate(
    private val coroutineScope: CoroutineScope,
    private val scaffoldState: ScaffoldState,
    private val navController: NavController,
    private val context: Context
) {

    fun showMessageToUser(uiText: UiText) {
        coroutineScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = uiText.asString(context),
                duration = SnackbarDuration.Short
            )
        }
    }

    fun navigateSingleTop(uiEvent: UiEvent.Navigate) {
        navController.navigate(uiEvent.route) {
            launchSingleTop = true
        }
    }

    fun navigate(uiEvent: UiEvent.Navigate) {
        navController.navigate(uiEvent.route)
    }

    fun navigate(route: String) {
        navController.navigate(route)
    }

    fun navigateUp() {
        navController.navigateUp()
    }

    fun launchPhotoDetailsGallery(property: Property) {
        launchPhotoGalleryActivity(context, property)

    }
}