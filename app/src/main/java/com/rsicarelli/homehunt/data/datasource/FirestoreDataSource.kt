package com.rsicarelli.homehunt.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.rsicarelli.homehunt.data.datasource.FirestoreDataSourceImpl.FirestoreMap.PROPERTY_COLLECTION
import com.rsicarelli.homehunt.domain.model.Mapper
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.model.Property.Tag.RENTED
import com.rsicarelli.homehunt.domain.model.Property.Tag.RESERVED
import com.rsicarelli.homehunt.domain.model.toProperty
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

interface FirestoreDataSource {
    suspend fun new(): Flow<List<Property>>
    suspend fun getById(referenceId: String): Flow<Property>
    fun toggleFavourite(referenceId: String, isFavourited: Boolean)
    suspend fun getFavourites(): Flow<List<Property>>
}

@OptIn(ExperimentalCoroutinesApi::class)
class FirestoreDataSourceImpl(
    private val db: FirebaseFirestore
) : FirestoreDataSource {

    override suspend fun new(): Flow<List<Property>> {
        return callbackFlow {
            val propertiesDocument = db.collection(PROPERTY_COLLECTION)
                .whereNotEqualTo(Mapper.TAG, listOf(RESERVED.identifier, RENTED.identifier))
                .whereEqualTo(Mapper.IS_ACTIVE, true)

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
                    ?.filter { property ->
                        //TODO replace with userId
                        property.viewedBy.filter { it.equals("7iyzHmMDxEOa5jAgF9kI7tNImmq1") }
                            .isNullOrEmpty()
                    }
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