package com.rsicarelli.homehunt.presentation.propertyDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.rsicarelli.homehunt.core.model.HomeHuntState
import com.rsicarelli.homehunt.presentation.propertyDetail.components.GalleryBottomSheet
import com.rsicarelli.homehunt.presentation.propertyDetail.components.PropertyDetail
import com.rsicarelli.homehunt.presentation.propertyDetail.components.PropertyTopBar
import com.rsicarelli.homehunt.presentation.propertyDetail.components.PropertyVideoBottomSheet

@Composable
fun PropertyDetailScreen(
    homeHuntState: HomeHuntState
) {
    val viewModel: PropertyDetailViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()

    PropertyDetailContent(
        state = state.value,
        events = viewModel::onEvent,
        homeHuntState = homeHuntState
    )
}

@Composable
private fun PropertyDetailContent(
    state: PropertyDetailState,
    events: (PropertyDetailEvents) -> Unit,
    homeHuntState: HomeHuntState
) {
    state.property?.let { property ->
        Box(modifier = Modifier.fillMaxSize()) {
            PropertyDetail(
                property = property,
                onOpenGallery = {
                    events(PropertyDetailEvents.OpenGallery)
                },
                onPlayVideo = {
                    events(PropertyDetailEvents.OpenVideoPreview)
                }
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
                },
                onBackClicked = {
                    homeHuntState.navigateUp()
                }
            )
            GalleryBottomSheet(
                photosGalleryUrls = property.photoGalleryUrls,
                isEnabled = state.openGallery,
                onCollapsed = {
                    events(PropertyDetailEvents.CloseGallery)
                }
            )
            PropertyVideoBottomSheet(
                videoUrl = property.videoUrl,
                isEnabled = state.openVideoPreview,
                onCollapsed = {
                    events(PropertyDetailEvents.CloseVideoPreview)
                }
            )
        }
    }
}

