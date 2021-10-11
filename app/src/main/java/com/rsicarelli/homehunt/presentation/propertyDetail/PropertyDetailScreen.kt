package com.rsicarelli.homehunt.presentation.propertyDetail

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.presentation.propertyDetail.components.*

@Composable
fun PropertyDetailScreen(
    scaffoldDelegate: ScaffoldDelegate,
    viewModel: PropertyDetailViewModel = hiltViewModel()
) {
    PropertyDetailContent(
        scaffoldDelegate = scaffoldDelegate,
        state = viewModel.state.value,
        events = viewModel::onEvent
    )
}

@OptIn(ExperimentalCoilApi::class, ExperimentalMaterialApi::class)
@Composable
private fun PropertyDetailContent(
    scaffoldDelegate: ScaffoldDelegate,
    state: PropertyDetailState,
    events: (PropertyDetailEvents) -> Unit
) {
    state.property?.let { property ->
        Box(modifier = Modifier.fillMaxSize()) {
            PropertyDetail(
                property = property,
                scaffoldDelegate = scaffoldDelegate,
                events = events,
            )
            PropertyTopBar(
                isFavourited = property.isFavourited,
                onFavouriteClick = {
                    events(
                        PropertyDetailEvents.ToggleFavourite(
                            referenceId = property.reference,
                            isFavourited = !property.isFavourited
                        )
                    )
                }
            )
            GalleryBottomSheet(
                photosGalleryUrls = property.photoGalleryUrls,
                isEnabled = state.openGallery,
                onCollapsed = {
                    events(PropertyDetailEvents.CloseGallery)
                }
            )
        }
    }
}

