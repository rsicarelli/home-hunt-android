package com.rsicarelli.homehunt.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.domain.usecase.GetFilteredPropertiesUseCase
import com.rsicarelli.homehunt.domain.usecase.ToggleFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase,
    private val toggleFavourite: ToggleFavouriteUseCase,
) : ViewModel() {

    fun loadProperties() = flow {
        getFilteredPropertiesUseCase(Unit)
            .onStart { emit(HomeState.Loading) }
            .collect { outcome ->
                if (outcome.properties.isNotEmpty()) {
                    emit(HomeState.Loaded(outcome.properties))
                } else {
                    emit(HomeState.EmptyResults)
                }
            }
    }

    fun toggleFavourite(referenceId: String, isFavourited: Boolean) {
        viewModelScope.launch {
            toggleFavourite(
                request = ToggleFavouriteUseCase.Request(
                    referenceId,
                    isFavourited
                )
            ).single()
        }
    }

}