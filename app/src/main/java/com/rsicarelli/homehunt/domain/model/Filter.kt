package com.rsicarelli.homehunt.domain.model

data class Filter(
    val priceRange: Pair<Double, Double>,
    val surfaceRange: Pair<Int, Int>,
    val dormSelection: List<Int>,
    val bathSelection: List<Int>
) {
    val maxDormCount = 5
    val maxBathCount = 5
    val maxPrice = 1600.0
    val maxSurface = 180
}