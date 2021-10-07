package com.rsicarelli.homehunt.presentation.propertyDetail

sealed class PropertyDetailEvents {
    data class GetPropertyFromCache(val referenceId: String) : PropertyDetailEvents()
}