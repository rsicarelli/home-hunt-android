package com.rsicarelli.homehunt.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.core.model.UiText
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.usecase.GetFilteredPropertiesUseCase
import com.rsicarelli.homehunt.domain.usecase.ToggleFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase,
    private val toggleFavourite: ToggleFavouriteUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    fun onEvent(events: HomeEvents) {
        when (events) {
            HomeEvents.GetProperties -> getProperties()
            is HomeEvents.ToggleFavourite -> toggleFavourite(events)
        }
    }

    private fun getProperties() {
        viewModelScope.launch {
            getFilteredPropertiesUseCase(Unit)
                .onStart { toggleLoading(ProgressBarState.Loading) }
                .onEach { outcome ->
                    updateProperties(outcome.properties)
                        .also { toggleLoading(ProgressBarState.Idle) }
                }
                .catch { showError().also { Timber.e("Error trying to get properties", it) } }
                .launchIn(viewModelScope)
        }
    }

    private fun toggleFavourite(events: HomeEvents.ToggleFavourite) {
        toggleFavourite(
            request = ToggleFavouriteUseCase.Request(
                events.reference,
                events.isFavourited
            )
        )
    }

    private fun updateProperties(properties: List<Property>) {
        _state.value = state.value.copy(
            properties = properties,
            emptyResults = properties.isEmpty(),
            progressBarState = ProgressBarState.Idle
        )
    }

    private fun toggleLoading(progressBarState: ProgressBarState) {
        _state.value = state.value.copy(progressBarState = progressBarState)
    }

    private fun showError() {
        _state.value =
            state.value.copy(uiEvent = UiEvent.MessageToUser(UiText.StringResource(R.string.error_loading_properties)))
    }
}