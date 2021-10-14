package com.rsicarelli.homehunt.presentation.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.usecase.GetFavouritedPropertiesUseCase
import com.rsicarelli.homehunt.domain.usecase.ToggleFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavourites: GetFavouritedPropertiesUseCase,
    private val toggleFavourite: ToggleFavouriteUseCase,
) : ViewModel() {
    private val _state: MutableStateFlow<FavouritesState> = MutableStateFlow(FavouritesState())
    val state: StateFlow<FavouritesState> = _state

    fun onEvent(event: FavouritesEvents) {
        when (event) {
            is FavouritesEvents.GetPropertiesFromCache -> getFavouritesFromCache()
            is FavouritesEvents.ToggleFavourite -> toggleFavourite(
                request = ToggleFavouriteUseCase.Request(
                    event.referenceId,
                    event.isFavourited
                )
            )
        }
    }

    private fun getFavouritesFromCache() {
        viewModelScope.launch {
            getFavourites().onEach { dataState ->
                updateProperty(dataState)
            }.launchIn(viewModelScope)
        }
    }

    private fun updateProperty(properties: List<Property>) {
        _state.value = state.value.copy(
            properties = properties,
            progressBarState = ProgressBarState.Idle,
            emptyResults = properties.isEmpty()
        )
    }

}