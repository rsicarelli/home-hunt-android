package com.rsicarelli.homehunt.domain.repository

import com.rsicarelli.homehunt.domain.model.Property
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    suspend fun getNewProperties(userId: String): Flow<List<Property>>
    suspend fun getBy(referenceId: String): Flow<Property>
    suspend fun getFavourites(): Flow<List<Property>>
    fun toggleFavourite(referenceId: String, isFavourited: Boolean)
    fun markAsViewed(referenceId: String, userId: String)
}