package com.rsicarelli.homehunt.di

import com.google.firebase.auth.FirebaseAuth
import com.rsicarelli.homehunt.data.datasource.FilterLocalDataSource
import com.rsicarelli.homehunt.data.repository.UserRepositoryImpl
import com.rsicarelli.homehunt.domain.repository.PropertyRepository
import com.rsicarelli.homehunt.domain.repository.UserRepository
import com.rsicarelli.homehunt.domain.usecase.*
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

    @Provides
    @Singleton
    fun providesFilterPropertiesUseCase() = FilterPropertiesUseCase()

    @Provides
    @Singleton
    fun providesPreviewFilterResultUseCase(
        propertyRepository: PropertyRepository,
        filterPropertiesUseCase: FilterPropertiesUseCase
    ) = PreviewFilterResultUseCase(propertyRepository, filterPropertiesUseCase)


    @Provides
    @Singleton
    fun providesGetFilterPreferencesUseCase(filterLocalDataSource: FilterLocalDataSource) =
        GetFilterPreferencesUseCase(filterLocalDataSource)

    @Provides
    @Singleton
    fun providesSaveFilterPreferencesUseCase(filterLocalDataSource: FilterLocalDataSource) =
        SaveFilterPreferencesUseCase(filterLocalDataSource)
}