package com.rsicarelli.homehunt.domain.model

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.rsicarelli.homehunt.domain.strategy.Filter
import com.rsicarelli.homehunt.domain.strategy.allFilters

data class SearchOption(
    val priceRange: Pair<Double, Double>,
    val surfaceRange: Pair<Int, Int>,
    val dormSelection: List<Int>,
    val bathSelection: List<Int>,
    val seenOnly: PropertyVisibility.Seen?,
    val notSeenOnly: PropertyVisibility.NotSeen?,
    val longTermOnly: Boolean,
    val showRented: Boolean,
    val showReserved: Boolean
) {
    private val filters: List<Filter> = allFilters
    val userId = Firebase.auth.uid //should not be here, refactor later

    fun applyFilter(properties: List<Property>): List<Property> {
        return properties.filter { property ->
            filters.all {
                it.applyFilter(this, property)
            }
        }
    }
}

sealed class PropertyVisibility {
    object Seen : PropertyVisibility()
    object NotSeen : PropertyVisibility()
}