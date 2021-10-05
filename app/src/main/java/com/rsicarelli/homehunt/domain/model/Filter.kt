package com.rsicarelli.homehunt.domain.model

data class Filter(
    val priceRange: Pair<Double, Double>,
    val surfaceRange: Pair<Int, Int>,
    val dormCount: List<Int>,
    val bathCount: List<Int>,
    val location: String
)