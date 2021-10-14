package com.rsicarelli.homehunt.presentation.propertyDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.domain.usecase.GetSinglePropertyUseCase
import com.rsicarelli.homehunt.domain.usecase.MarkAsViewedUseCase
import com.rsicarelli.homehunt.domain.usecase.ToggleFavouriteUseCase
import com.rsicarelli.homehunt.ui.navigation.NavArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PropertyDetailViewModel @Inject constructor(
    private val getSingleProperty: GetSinglePropertyUseCase,
    private val toggleFavourite: ToggleFavouriteUseCase,
    private val markAsViewed: MarkAsViewedUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state: MutableStateFlow<PropertyDetailState> =
        MutableStateFlow(PropertyDetailState())
    val state: StateFlow<PropertyDetailState> = _state

    init {
        savedStateHandle.get<String>(NavArguments.PROPERTY_DETAIL)?.let { referenceId ->
            onEvent(PropertyDetailEvents.GetPropertyFromCache(referenceId))
            onEvent(PropertyDetailEvents.MarkAsViewed(referenceId))
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
            is PropertyDetailEvents.MarkAsViewed -> markAsViewed(
                request = MarkAsViewedUseCase.Request(
                    event.referenceId
                )
            )
            PropertyDetailEvents.CloseGallery -> _state.value = state.value.copy(
                openGallery = false
            )
            PropertyDetailEvents.OpenGallery -> _state.value = state.value.copy(
                openGallery = true
            )
            PropertyDetailEvents.CloseVideoPreview -> _state.value = state.value.copy(
                openVideoPreview = false
            )
            PropertyDetailEvents.OpenVideoPreview -> _state.value = state.value.copy(
                openVideoPreview = true
            )
        }
    }

    private fun getPropertyFromCache(referenceId: String) {
        viewModelScope.launch {
            getSingleProperty(GetSinglePropertyUseCase.Request(referenceId)).first().run {
                _state.value = state.value.copy(
                    property = this.property,
                    progressBarState = ProgressBarState.Idle
                )
            }
        }
    }

}



