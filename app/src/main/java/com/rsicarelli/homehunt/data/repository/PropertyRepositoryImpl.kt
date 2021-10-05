package com.rsicarelli.homehunt.data.repository

import com.rsicarelli.homehunt.data.FirestoreDataSource
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PropertyRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) : PropertyRepository {
    override suspend fun getAll(): Flow<List<Property>> {
        return firestoreDataSource.new()
    }
}