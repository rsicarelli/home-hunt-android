package com.rsicarelli.homehunt.domain.repository

import com.rsicarelli.homehunt.domain.model.Property
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    suspend fun getAll(): Flow<List<Property>>
    suspend fun getBy(referenceId: String): Flow<Property>
    fun toggleFavourite(referenceId: String, isFavourited: Boolean)
}