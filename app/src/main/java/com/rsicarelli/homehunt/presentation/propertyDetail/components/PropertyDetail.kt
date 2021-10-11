package com.rsicarelli.homehunt.presentation.propertyDetail.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ScaffoldDelegate
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.presentation.components.GalleryCarousel
import com.rsicarelli.homehunt.presentation.propertyDetail.PropertyDetailEvents

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PropertyDetail(
    property: Property,
    onOpenGallery: () -> Unit,
    onPlayVideo: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            GalleryCarousel(
                photoGallery = property.photoGalleryUrls,
                hasVideo = property.videoUrl != null && property.videoUrl.isNotEmpty(),
                onOpenGallery = onOpenGallery,
                onPlayVideo = onPlayVideo
            )
        }
        item {
            PropertyHeader(property = property)
        }
        item {
            PropertyFeatures(characteristics = property.characteristics)
        }
        item {
            PropertyMap(
                lat = property.lat,
                lng = property.lng,
                onMapClick = {
                    //TODO
                }
            )
        }
        item {
            PropertyContentDescription(
                titleRes = R.string.about_this_property,
                content = property.fullDescription
            )
        }
        item {
            PropertyContentDescription(
                titleRes = R.string.location_description,
                content = property.locationDescription
            )
        }
        item {
            PropertyFooter(
                reference = property.reference,
                propertyUrl = property.propertyUrl,
                pdfUrl = property.pdfUrl
            )
        }
    }
}