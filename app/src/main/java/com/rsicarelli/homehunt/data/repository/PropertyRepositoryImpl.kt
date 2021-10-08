package com.rsicarelli.homehunt.data.repository

import com.rsicarelli.homehunt.data.datasource.FirestoreDataSource
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PropertyRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) : PropertyRepository {
    override suspend fun getNewProperties(userId: String): Flow<List<Property>> =
        firestoreDataSource.getNewProperties(userId)

    override suspend fun getBy(referenceId: String): Flow<Property> =
        firestoreDataSource.getById(referenceId)

    override suspend fun getFavourites(): Flow<List<Property>> = firestoreDataSource.getFavourites()

    override fun toggleFavourite(referenceId: String, isFavourited: Boolean) =
        firestoreDataSource.toggleFavourite(referenceId, isFavourited)

    override fun markAsViewed(referenceId: String, userId: String) =
        firestoreDataSource.markAsViewed(referenceId, userId)

}