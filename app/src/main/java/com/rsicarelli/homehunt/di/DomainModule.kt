package com.rsicarelli.homehunt.di

import com.google.firebase.auth.FirebaseAuth
import com.rsicarelli.homehunt.data.repository.UserRepositoryImpl
import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import com.rsicarelli.homehunt.domain.repository.UserRepository
import com.rsicarelli.homehunt.domain.usecase.GetPropertiesUseCase
import com.rsicarelli.homehunt.domain.usecase.IsLoggedInUseCase
import com.rsicarelli.homehunt.domain.usecase.SignInUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun providesSignInUseCase(firebaseAuth: FirebaseAuth) = SignInUseCase(firebaseAuth)

    @Provides
    @Singleton
    fun providesIsLoggedInUseCase(userRepository: UserRepository) =
        IsLoggedInUseCase(userRepository)

    @Provides
    @Singleton
    fun providesGetPropertiesUseCase(propertiesRepository: PropertyRepository) =
        GetPropertiesUseCase(propertiesRepository)
}