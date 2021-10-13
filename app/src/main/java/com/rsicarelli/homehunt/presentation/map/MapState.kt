package com.rsicarelli.homehunt.presentation.map

import com.rsicarelli.homehunt.domain.model.Property

data class MapState(
    val properties: List<Property> = emptyList()
)