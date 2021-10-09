package com.rsicarelli.homehunt.data.datasource

import android.content.SharedPreferences
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
            putFloat(Mapper.minPrice, searchOption.priceRange.first.toFloat())
            putFloat(Mapper.maxPrice, searchOption.priceRange.second.toFloat())
            putInt(Mapper.minSurface, searchOption.surfaceRange.first)
            putInt(Mapper.maxSurface, searchOption.surfaceRange.second)
            putStringSet(
                Mapper.dormSelection,
                searchOption.dormSelection.map { it.toString() }.toSet()
            )
            putStringSet(
                Mapper.bathSelection,
                searchOption.bathSelection.map { it.toString() }.toSet()
            )
            putBoolean(Mapper.notSeenOnly, searchOption.notSeenOnly)
            putBoolean(Mapper.seenOnly, searchOption.seenOnly)
            putBoolean(Mapper.seenAndNotSeen, searchOption.seenAndNotSeen)
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
            val dormSelection =
                getStringSet(Mapper.dormSelection, emptySet())?.mapNotNull { it.toInt() }
                    ?: emptyList()
            val bathSelection =
                getStringSet(Mapper.bathSelection, emptySet())?.map { it.toInt() } ?: emptyList()

            val notSeenOnly = getBoolean(Mapper.notSeenOnly, false)
            val seenOnly = getBoolean(Mapper.seenOnly, false)
            val seenAndNotSeen = getBoolean(Mapper.seenAndNotSeen, true)
            val longTermOnly = getBoolean(Mapper.longTermOnly, false)
            val showReserved = getBoolean(Mapper.showReserved, false)
            val showRented = getBoolean(Mapper.showRented, false)

            return SearchOption(
                priceRange = Pair(minPrice.toDouble(), maxPrice.toDouble()),
                surfaceRange = Pair(minSurface, maxSurface),
                dormSelection = dormSelection,
                bathSelection = bathSelection,
                notSeenOnly = notSeenOnly,
                seenOnly = seenOnly,
                seenAndNotSeen = seenAndNotSeen,
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
        const val dormSelection = "PREF_DORM_SELECTION"
        const val bathSelection = "PREF_BATH_SELECTION"
        const val longTermOnly = "PREF_LONG_TERM_ONLY"
        const val notSeenOnly = "PREF_NOT_SEEN_ONLY"
        const val seenAndNotSeen = "PREF_SEEN_AND_NOT_SEEN"
        const val seenOnly = "PREF_SEEN_ONLY"
        const val showReserved = "PREF_SHOW_RESERVED"
        const val showRented = "PREF_SHOW_RENTED"
    }
}