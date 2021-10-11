package com.rsicarelli.homehunt.presentation.propertyDetail

sealed class PropertyDetailEvents {
    object OpenGallery : PropertyDetailEvents()
    object CloseGallery : PropertyDetailEvents()
    object OpenVideoPreview : PropertyDetailEvents()
    object CloseVideoPreview : PropertyDetailEvents()

    data class GetPropertyFromCache(val referenceId: String) : PropertyDetailEvents()
    data class ToggleFavourite(val referenceId: String, val isFavourited: Boolean) :
        PropertyDetailEvents()

    data class MarkAsViewed(val referenceId: String) : PropertyDetailEvents()
}