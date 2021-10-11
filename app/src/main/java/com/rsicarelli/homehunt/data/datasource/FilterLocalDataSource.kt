package com.rsicarelli.homehunt.data.datasource

import android.content.SharedPreferences
import androidx.core.content.edit
import com.rsicarelli.homehunt.domain.model.SearchOption

interface FilterLocalDataSource {
    fun save(searchOption: SearchOption)
    fun get(): SearchOption
}

class FilterLocalDataSourceImpl(
    private val sharedPreferences: SharedPreferences
) : FilterLocalDataSource {
    override fun save(searchOption: SearchOption) {
        sharedPreferences.edit(commit = true) {
            with(searchOption) {
                putFloat(Mapper.minPrice, priceRange.first.toFloat())
                putFloat(Mapper.maxPrice, priceRange.second.toFloat())
                putInt(Mapper.minSurface, surfaceRange.first)
                putInt(Mapper.maxSurface, surfaceRange.second)
                putInt(Mapper.dormCount, dormCount)
                putInt(Mapper.bathCount, bathCount)
                putBoolean(Mapper.showSeen, showSeen)
                putBoolean(Mapper.longTermOnly, longTermOnly)
                putBoolean(Mapper.showRented, showRented)
                putBoolean(Mapper.showReserved, showReserved)
            }
        }
    }

    override fun get(): SearchOption {
        with(sharedPreferences) {
            val minPrice = getFloat(Mapper.minPrice, 0.0f).toDouble()
            val maxPrice = getFloat(Mapper.maxPrice, 99999.0F).toDouble()
            val minSurface = getInt(Mapper.minSurface, 0)
            val maxSurface = getInt(Mapper.maxSurface, 99999)
            val dormSelection = getInt(Mapper.dormCount, 0)
            val bathSelection = getInt(Mapper.bathCount, 0)
            val showNotSeen = getBoolean(Mapper.showSeen, false)
            val longTermOnly = getBoolean(Mapper.longTermOnly, false)
            val showReserved = getBoolean(Mapper.showReserved, false)
            val showRented = getBoolean(Mapper.showRented, false)

            return SearchOption(
                priceRange = Pair(minPrice, maxPrice),
                surfaceRange = Pair(minSurface, maxSurface),
                dormCount = dormSelection,
                bathCount = bathSelection,
                showSeen = showNotSeen,
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
        const val showSeen = "PREF_SHOW_SEEN"
        const val showReserved = "PREF_SHOW_RESERVED"
        const val showRented = "PREF_SHOW_RENTED"
    }
}