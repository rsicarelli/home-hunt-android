package com.rsicarelli.homehunt.core.di

import com.google.firebase.auth.FirebaseAuth
import com.rsicarelli.homehunt.data.datasource.FilterLocalDataSource
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
    fun providesGetPropertiesUseCase(
        propertiesRepository: PropertyRepository,
        userRepository: UserRepository
    ) =
        GetAllPropertiesUseCase(propertiesRepository, userRepository)

    @Provides
    @Singleton
    fun providesGetSinglePropertyUseCase(propertiesRepository: PropertyRepository) =
        GetSinglePropertyUseCase(propertiesRepository)

    @Provides
    @Singleton
    fun providesToggleFavouriteUseCase(propertiesRepository: PropertyRepository) =
        ToggleFavouriteUseCase(propertiesRepository)

    @Provides
    @Singleton
    fun providesMarkAsViewedUseCase(
        propertiesRepository: PropertyRepository,
        userRepository: UserRepository
    ) = MarkAsViewedUseCase(propertiesRepository, userRepository)

    @Provides
    @Singleton
    fun providesGetFavouritedPropertiesUseCase(propertiesRepository: PropertyRepository) =
        GetFavouritedPropertiesUseCase(propertiesRepository)

    @Provides
    @Singleton
    fun providesFilterPropertiesUseCase() = FilterPropertiesUseCase()

    @Provides
    @Singleton
    fun providesPreviewFilterResultUseCase(
        propertyRepository: PropertyRepository,
        userRepository: UserRepository,
        filterPropertiesUseCase: FilterPropertiesUseCase
    ) = PreviewFilterResultUseCase(propertyRepository, userRepository, filterPropertiesUseCase)

    @Provides
    @Singleton
    fun providesGetFilterPreferencesUseCase(filterLocalDataSource: FilterLocalDataSource) =
        GetFilterPreferencesUseCase(filterLocalDataSource)

    @Provides
    @Singleton
    fun providesSaveFilterPreferencesUseCase(filterLocalDataSource: FilterLocalDataSource) =
        SaveFilterPreferencesUseCase(filterLocalDataSource)

    @Provides
    fun providesGetFilteredPropertiesUseCase(
        getAllAllProperties: GetAllPropertiesUseCase,
        getFilterPreferences: GetFilterPreferencesUseCase,
        filterProperties: FilterPropertiesUseCase,
    ) = GetFilteredPropertiesUseCase(
        getAllAllProperties,
        getFilterPreferences,
        filterProperties
    )
}