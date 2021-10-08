package com.rsicarelli.homehunt.presentation.favourites

sealed class FavouritesEvents {
    object GetPropertiesFromCache : FavouritesEvents()
    data class ToggleFavourite(val referenceId: String, val isFavourited: Boolean) :
        FavouritesEvents()
}