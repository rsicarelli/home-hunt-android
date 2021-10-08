package com.rsicarelli.homehunt.presentation.favourites

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.usecase.GetFavouritedPropertiesUseCase
import com.rsicarelli.homehunt.domain.usecase.ToggleFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val getFavourites: GetFavouritedPropertiesUseCase,
    private val toggleFavourite: ToggleFavouriteUseCase,
) : ViewModel() {
    private val _state: MutableState<FavouritesState> = mutableStateOf(FavouritesState())
    val state: State<FavouritesState> = _state

    init {
        onEvent(FavouritesEvents.GetPropertiesFromCache)
    }

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
                println(dataState)
                when (dataState) {
                    is DataState.Data -> state.updateProperty(dataState.data!!)
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun State<FavouritesState>.updateProperty(properties: List<Property>) {
        _state.value = this.value.copy(
            properties = properties,
            progressBarState = ProgressBarState.Idle,
            emptyResults = properties.isEmpty()
        )
    }

}