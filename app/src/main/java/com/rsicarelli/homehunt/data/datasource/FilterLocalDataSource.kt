package com.rsicarelli.homehunt.data.datasource

import android.content.SharedPreferences
import com.rsicarelli.homehunt.domain.model.Filter

interface FilterLocalDataSource {
    fun save(filter: Filter)
    fun get(): Filter
}

class FilterLocalDataSourceImpl(
    private val sharedPreferences: SharedPreferences
) : FilterLocalDataSource {
    override fun save(filter: Filter) {
        with(sharedPreferences.edit()) {
            putFloat(Mapper.minPrice, filter.priceRange.first.toFloat())
            putFloat(Mapper.maxPrice, filter.priceRange.second.toFloat())
            putInt(Mapper.minSurface, filter.surfaceRange.first)
            putInt(Mapper.maxSurface, filter.surfaceRange.second)
            putStringSet(Mapper.dormSelection, filter.dormSelection.map { it.toString() }.toSet())
            putStringSet(Mapper.bathSelection, filter.bathSelection.map { it.toString() }.toSet())
            commit()
        }
    }

    override fun get(): Filter {
        with(sharedPreferences) {
            val minPrice = getFloat(Mapper.minPrice, 0.0f)
            val maxPrice = getFloat(Mapper.maxPrice, 99999.0F)
            val minSurface = getInt(Mapper.minSurface, 0)
            val maxSurface = getInt(Mapper.maxSurface, 99999)
            val dormSelection =
                getStringSet(Mapper.dormSelection, emptySet())?.mapNotNull { it.toInt() }
                    ?: emptyList()
            val bathSelection =
                getStringSet(Mapper.bathSelection, emptySet())?.map { it.toInt() } ?: emptyList()

            return Filter(
                priceRange = Pair(minPrice.toDouble(), maxPrice.toDouble()),
                surfaceRange = Pair(minSurface, maxSurface),
                dormSelection = dormSelection,
                bathSelection = bathSelection
            )
        }
    }

    private object Mapper {
        const val minPrice = "PREF_MIN_PRICE"
        const val maxPrice = "PREF_MAX_PRICE"
        const val minSurface = "PREF_MIN_SURFACE"
        const val maxSurface = "PREF_MAX_SURFACE"
        const val dormSelection = "PREF_DORM_SELECTION"
        const val bathSelection = "PREF_BATH_SELECTION"
    }
}