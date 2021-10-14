package com.rsicarelli.homehunt.presentation.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.domain.usecase.GetFilterPreferencesUseCase
import com.rsicarelli.homehunt.domain.usecase.PreviewFilterResultUseCase
import com.rsicarelli.homehunt.domain.usecase.SaveFilterPreferencesUseCase
import com.rsicarelli.homehunt.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val previewFilterResult: PreviewFilterResultUseCase,
    private val getFilter: GetFilterPreferencesUseCase,
    private val saveFilter: SaveFilterPreferencesUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<FilterState> = MutableStateFlow(FilterState())
    val state: StateFlow<FilterState> = _state

    fun onEvent(events: FilterEvents) {
        when (events) {
            is FilterEvents.GetFilter -> getFilter()
            FilterEvents.ClearFilter -> TODO()
            FilterEvents.SaveFilter -> onSaveFilter()
            is FilterEvents.PriceRangeChanged -> priceRangeChanged(events)
            is FilterEvents.DormsSelectionChanged -> dormsSelectionChanged(events)
            is FilterEvents.SurfaceRangeChanged -> surfaceRangeChanged(events)
            is FilterEvents.BathSelectionChanged -> bathSelectionChanged(events)
            is FilterEvents.VisibilitySelectionChanged -> handleVisibilityChanged(events.newValue)
            is FilterEvents.LongerTermRentalSelectionChanged -> handleLongTermChanged(events.newValue)
            is FilterEvents.AvailabilitySelectionChanged -> handleAvailabilityChanged(events.newValue)
        }
        previewResults()
    }

    private fun getFilter() {
        viewModelScope.launch {
            val result = getFilter(Unit).first()
            _state.value = state.value.fromFilter(result.searchOption)
        }
    }

    private fun handleAvailabilityChanged(newValue: Boolean) {
        _state.value = state.value.copy(availableOnly = newValue)
    }

    private fun handleLongTermChanged(newValue: Boolean) {
        _state.value = state.value.copy(longTermOnly = newValue)
    }

    private fun handleVisibilityChanged(newValue: Boolean) {
        _state.value = state.value.copy(showSeen = newValue)
    }

    private fun onSaveFilter() {
        viewModelScope.launch {
            saveFilter(SaveFilterPreferencesUseCase.Request(state.value.toFilter())).collect {
                _state.value = state.value.copy(uiEvent = UiEvent.Navigate(Screen.Home.route))
            }
        }
    }

    private fun bathSelectionChanged(events: FilterEvents.BathSelectionChanged) {
        _state.value = state.value.copy(bathCount = events.newValue)
    }

    private fun surfaceRangeChanged(events: FilterEvents.SurfaceRangeChanged) {
        _state.value = state.value.copy(surfaceRange = events.range)
    }

    private fun dormsSelectionChanged(events: FilterEvents.DormsSelectionChanged) {
        _state.value = state.value.copy(dormCount = events.newValue)
    }

    private fun priceRangeChanged(events: FilterEvents.PriceRangeChanged) {
        _state.value = state.value.copy(priceRange = events.range)
    }

    private fun previewResults() {
        viewModelScope.launch {
            delay(1000)
            val single = previewFilterResult(
                PreviewFilterResultUseCase.Request(
                    state.value.toFilter()
                )
            ).first()

            _state.value =
                state.value.copy(previewResultCount = (single.properties.size))
        }
    }
}