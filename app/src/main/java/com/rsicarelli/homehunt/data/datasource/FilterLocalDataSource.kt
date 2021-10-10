package com.rsicarelli.homehunt.data.datasource

import android.content.SharedPreferences
import com.rsicarelli.homehunt.domain.model.PropertyVisibility
import com.rsicarelli.homehunt.domain.model.SearchOption

interface FilterLocalDataSource {
    fun save(searchOption: SearchOption)
    fun get(): SearchOption
}

class FilterLocalDataSourceImpl(
    private val sharedPreferences: SharedPreferences
) : FilterLocalDataSource {
    override fun save(searchOption: SearchOption) {
        with(sharedPreferences.edit()) {
            val notSeen = searchOption.notSeenOnly != null
            val seen = searchOption.seenOnly != null

            putFloat(Mapper.minPrice, searchOption.priceRange.first.toFloat())
            putFloat(Mapper.maxPrice, searchOption.priceRange.second.toFloat())
            putInt(Mapper.minSurface, searchOption.surfaceRange.first)
            putInt(Mapper.maxSurface, searchOption.surfaceRange.second)
            putInt(Mapper.dormCount, searchOption.dormCount)
            putInt(Mapper.bathCount, searchOption.bathCount)
            putBoolean(Mapper.notSeenOnly, notSeen)
            putBoolean(Mapper.seenOnly, seen)
            putBoolean(Mapper.longTermOnly, searchOption.longTermOnly)
            putBoolean(Mapper.showRented, searchOption.showRented)
            putBoolean(Mapper.showReserved, searchOption.showReserved)
            commit()
        }
    }

    override fun get(): SearchOption {
        with(sharedPreferences) {
            val minPrice = getFloat(Mapper.minPrice, 0.0f)
            val maxPrice = getFloat(Mapper.maxPrice, 99999.0F)
            val minSurface = getInt(Mapper.minSurface, 0)
            val maxSurface = getInt(Mapper.maxSurface, 99999)
            val dormSelection = getInt(Mapper.dormCount, 0)
            val bathSelection = getInt(Mapper.bathCount, 0)
            val notSeenOnly = getBoolean(Mapper.notSeenOnly, false)
            val seenOnly = getBoolean(Mapper.seenOnly, false)
            val longTermOnly = getBoolean(Mapper.longTermOnly, false)
            val showReserved = getBoolean(Mapper.showReserved, false)
            val showRented = getBoolean(Mapper.showRented, false)

            return SearchOption(
                priceRange = Pair(minPrice.toDouble(), maxPrice.toDouble()),
                surfaceRange = Pair(minSurface, maxSurface),
                dormCount = dormSelection,
                bathCount = bathSelection,
                seenOnly = if (seenOnly) PropertyVisibility.Seen else null,
                notSeenOnly = if (notSeenOnly) PropertyVisibility.NotSeen else null,
                longTermOnly = longTermOnly,
                showRented = showRented,
                showReserved = showReserved
            )
        }
    }

    private object Mapper {
        const val minPrice = "PREF_MIN_PRICE"
        const val maxPrice = "PREF_MAX_PRICE"
        const val minSurface = "PREF_MIN_SURFACE"
        const val maxSurface = "PREF_MAX_SURFACE"
        const val dormCount = "PREF_DORM_COUNT"
        const val bathCount = "PREF_BATH_COUNT"
        const val longTermOnly = "PREF_LONG_TERM_ONLY"
        const val notSeenOnly = "PREF_NOT_SEEN_ONLY"
        const val seenOnly = "PREF_SEEN_ONLY"
        const val showReserved = "PREF_SHOW_RESERVED"
        const val showRented = "PREF_SHOW_RENTED"
    }
}