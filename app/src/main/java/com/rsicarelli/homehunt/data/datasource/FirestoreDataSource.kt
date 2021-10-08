package com.rsicarelli.homehunt.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.rsicarelli.homehunt.data.datasource.FirestoreDataSourceImpl.FirestoreMap.PROPERTY_COLLECTION
import com.rsicarelli.homehunt.domain.model.Mapper
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.model.Property.Tag.RENTED
import com.rsicarelli.homehunt.domain.model.Property.Tag.RESERVED
import com.rsicarelli.homehunt.domain.model.toProperty
import com.rsicarelli.homehunt.domain.model.toTag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

interface FirestoreDataSource {
    suspend fun getNewProperties(userId: String): Flow<List<Property>>
    suspend fun getById(referenceId: String): Flow<Property>
    suspend fun getFavourites(): Flow<List<Property>>
    fun toggleFavourite(referenceId: String, isFavourited: Boolean)
    fun markAsViewed(referenceId: String, userId: String)
}

@OptIn(ExperimentalCoroutinesApi::class)
class FirestoreDataSourceImpl(
    private val db: FirebaseFirestore
) : FirestoreDataSource {

    override suspend fun getNewProperties(userId: String): Flow<List<Property>> {
        return callbackFlow {
            val propertiesDocument = db.collection(PROPERTY_COLLECTION)
                .whereEqualTo(Mapper.IS_ACTIVE, true)

            val subscription = propertiesDocument.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    cancel(
                        message = "error fetching collection data at path",
                        cause = error
                    )
                    return@addSnapshotListener
                }

                snapshot?.documents?.mapNotNull { documentSnapshot ->
                    documentSnapshot.data!!.toProperty()
                }
                    ?.filter {
                        val tag = it.tag.toTag()
                        tag != RENTED || tag != RESERVED
                    }?.filter {
                        !it.viewedBy.contains(userId)
                    }
                    ?.let { trySend(it) }
                    ?: close(Exception("Something wrong is not right"))
            }

            awaitClose { subscription.remove() }
        }
    }

    override suspend fun getById(referenceId: String): Flow<Property> {
        return callbackFlow {
            val document = db.collection(PROPERTY_COLLECTION).document(referenceId)

            val subscription = document.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    cancel(
                        message = "error fetching collection data at path",
                        cause = error
                    )
                    return@addSnapshotListener
                }

                snapshot?.data?.let {
                    trySend(it.toProperty())
//                    cancel("Done")
                } ?: cancel("Could not locate property reference")
            }

            awaitClose { subscription.remove() }
        }
    }

    override fun toggleFavourite(
        referenceId: String,
        isFavourited: Boolean
    ) {
        val document = db.collection(PROPERTY_COLLECTION).document(referenceId)
        document.update(Mapper.IS_FAVOURITED, isFavourited).addOnSuccessListener {
            Timber.d("Toggled favourite: $referenceId, $isFavourited")
        }
    }

    override fun markAsViewed(referenceId: String, userId: String) {
        val document = db.collection(PROPERTY_COLLECTION).document(referenceId)
        document.get().addOnSuccessListener {
            it.data?.toProperty()?.let { property ->
                val viewedBy = property.viewedBy.toMutableList().apply {
                    add(userId)
                }
                document.update(Mapper.VIEWED_BY, viewedBy).addOnSuccessListener {
                    Timber.d("Marked as viewed")
                }
            }
        }
    }

    override suspend fun getFavourites(): Flow<List<Property>> {
        return callbackFlow {
            val propertiesDocument = db.collection(PROPERTY_COLLECTION)
                .whereEqualTo(Mapper.IS_FAVOURITED, true)

            val subscription = propertiesDocument.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    cancel(
                        message = "error fetching collection data at path",
                        cause = error
                    )
                    return@addSnapshotListener
                }

                snapshot?.documents?.mapNotNull { it.data!!.toProperty() }
                    ?.let { trySend(it) }
                    ?: close(Exception("Something wrong is not right"))
            }

            awaitClose { subscription.remove() }
        }
    }


    private object FirestoreMap {
        const val PROPERTY_COLLECTION = "properties"
    }
}