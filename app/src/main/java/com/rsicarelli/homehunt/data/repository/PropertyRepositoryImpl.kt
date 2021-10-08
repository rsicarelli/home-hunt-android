package com.rsicarelli.homehunt.data.repository

import com.rsicarelli.homehunt.data.datasource.FirestoreDataSource
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PropertyRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) : PropertyRepository {
    override suspend fun getAll(): Flow<List<Property>> = firestoreDataSource.new()

    override suspend fun getBy(referenceId: String): Flow<Property> =
        firestoreDataSource.getById(referenceId)

    override fun toggleFavourite(referenceId: String, isFavourited: Boolean) =
        firestoreDataSource.toggleFavourite(referenceId, isFavourited)

    override suspend fun getFavourites(): Flow<List<Property>> = firestoreDataSource.getFavourites()
}