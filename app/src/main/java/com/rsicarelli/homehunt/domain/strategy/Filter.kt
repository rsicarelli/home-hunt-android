package com.rsicarelli.homehunt.domain.strategy

import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.model.Property.Tag
import com.rsicarelli.homehunt.domain.model.SearchOption
import com.rsicarelli.homehunt.domain.model.toTag

interface Filter {
    fun applyFilter(searchOption: SearchOption, property: Property): Boolean
}

val allFilters = listOf(
    Price,
    Surface,
    Dorm,
    Bath,
    Visibility,
    LongTermOnly,
    Rented,
    Reserved
)

private object Price : Filter {
    private const val UNLIMITED_PRICE = 99999.0
    private const val MAX_PRICE = 1600.0

    override fun applyFilter(searchOption: SearchOption, property: Property): Boolean {
        val (min, max) = searchOption.priceRange

        val range = if (max == MAX_PRICE) min..UNLIMITED_PRICE else min..max

        return property.price in range
    }
}

private object Surface : Filter {
    private const val UNLIMITED_SURFACE = 99999
    private const val MAX_SURFACE = 180

    override fun applyFilter(searchOption: SearchOption, property: Property): Boolean {
        val (min, max) = searchOption.surfaceRange

        val range = if (max == MAX_SURFACE) min..UNLIMITED_SURFACE else min..max

        return property.surface in range
    }
}

private object Dorm : Filter {
    override fun applyFilter(searchOption: SearchOption, property: Property): Boolean {
        return property.dormCount?.let {
            it >= searchOption.dormCount
        } ?: true
    }
}

private object Bath : Filter {
    override fun applyFilter(searchOption: SearchOption, property: Property): Boolean {
        return property.bathCount?.let {
            it >= searchOption.bathCount
        } ?: true
    }
}

private object Visibility : Filter {
    override fun applyFilter(searchOption: SearchOption, property: Property): Boolean {
        if (searchOption.showSeen) return true

        return !property.viewedBy.contains(searchOption.userId)
    }
}

private object LongTermOnly : Filter {
    override fun applyFilter(searchOption: SearchOption, property: Property): Boolean {
        if (!searchOption.longTermOnly) return true

        return property.fullDescription?.lowercase()?.contains("short term")?.not() ?: true
    }
}

private object Reserved : Filter {
    override fun applyFilter(searchOption: SearchOption, property: Property): Boolean {
        if (!searchOption.showReserved) return true

        return property.tag.toTag() == Tag.RESERVED
    }
}

private object Rented : Filter {
    override fun applyFilter(searchOption: SearchOption, property: Property): Boolean {
        if (!searchOption.showRented) return true

        return property.tag.toTag() == Tag.RENTED
    }
}
