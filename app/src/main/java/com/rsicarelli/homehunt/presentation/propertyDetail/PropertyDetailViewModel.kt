package com.rsicarelli.homehunt.presentation.propertyDetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.usecase.GetSinglePropertyUseCase
import com.rsicarelli.homehunt.domain.usecase.ToggleFavouriteUseCase
import com.rsicarelli.homehunt.presentation.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PropertyDetailViewModel @Inject constructor(
    private val getSingleProperty: GetSinglePropertyUseCase,
    private val toggleFavourite: ToggleFavouriteUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state: MutableState<PropertyDetailState> = mutableStateOf(PropertyDetailState())
    val state: State<PropertyDetailState> = _state

    init {
        savedStateHandle.get<String>("referenceId")?.let { referenceId ->
            onEvent(PropertyDetailEvents.GetPropertyFromCache(referenceId))
        } ?: {
            //TODO Error
        }
    }

    fun onEvent(event: PropertyDetailEvents) {
        when (event) {
            is PropertyDetailEvents.GetPropertyFromCache -> getPropertyFromCache(event.referenceId)
            is PropertyDetailEvents.ToggleFavourite -> toggleFavourite(
                request = ToggleFavouriteUseCase.Request(
                    event.referenceId,
                    event.isFavourited
                )
            )
        }
    }

    private fun getPropertyFromCache(referenceId: String) {
        viewModelScope.launch {
            getSingleProperty(GetSinglePropertyUseCase.Request(referenceId)).onEach { dataState ->
                println(dataState)
                when (dataState) {
                    is DataState.Data -> state.updateProperty(dataState.data!!)
                    is DataState.Loading -> state.toggleLoading(dataState.progressBarState)
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun State<PropertyDetailState>.updateProperty(property: Property) {
        _state.value = this.value.copy(
            property = property,
            progressBarState = ProgressBarState.Idle
        )
    }

    private fun <T> State<T>.toggleLoading(progressBarState: ProgressBarState) {

    }
}



