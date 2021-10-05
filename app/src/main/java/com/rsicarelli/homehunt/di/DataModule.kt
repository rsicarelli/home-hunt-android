package com.rsicarelli.homehunt.di

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rsicarelli.homehunt.data.FirestoreDataSource
import com.rsicarelli.homehunt.data.FirestoreDataSourceImpl
import com.rsicarelli.homehunt.data.datasource.FilterLocalDataSource
import com.rsicarelli.homehunt.data.datasource.FilterLocalDataSourceImpl
import com.rsicarelli.homehunt.data.repository.PropertyRepositoryImpl
import com.rsicarelli.homehunt.data.repository.UserRepositoryImpl
import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import com.rsicarelli.homehunt.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesFirestoreDataSource(firestore: FirebaseFirestore): FirestoreDataSource =
        FirestoreDataSourceImpl(firestore)

    @Provides
    @Singleton
    fun providesUserRepository(firebaseAuth: FirebaseAuth): UserRepository =
        UserRepositoryImpl(firebaseAuth)

    @Provides
    @Singleton
    fun providesPropertyRepository(firestoreDataSource: FirestoreDataSource): PropertyRepository =
        PropertyRepositoryImpl(firestoreDataSource)

    @Provides
    @Singleton
    fun providesFilterLocalDataSource(sharedPreferences: SharedPreferences): FilterLocalDataSource =
        FilterLocalDataSourceImpl(sharedPreferences)
}