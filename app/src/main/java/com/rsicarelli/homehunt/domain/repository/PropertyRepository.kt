package com.rsicarelli.homehunt.domain.repository

import com.rsicarelli.homehunt.domain.model.Property
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    suspend fun getAll(): Flow<List<Property>>
}