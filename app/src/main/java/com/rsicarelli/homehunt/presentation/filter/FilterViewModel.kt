package com.rsicarelli.homehunt.presentation.filter

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.domain.usecase.GetFilterPreferencesUseCase
import com.rsicarelli.homehunt.domain.usecase.PreviewFilterResultUseCase
import com.rsicarelli.homehunt.domain.usecase.SaveFilterPreferencesUseCase
import com.rsicarelli.homehunt.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private val _state: MutableState<FilterState> = mutableStateOf(FilterState())
    val state: State<FilterState> = _state

    fun onEvent(events: FilterEvents) {
        when (events) {
            FilterEvents.ClearFilter -> TODO()
            FilterEvents.SaveFilter -> onSaveFilter()
            is FilterEvents.PriceRangeChanged -> priceRangeChanged(events)
            is FilterEvents.DormsSelectedChange -> dormsSelectionChanged(events)
            is FilterEvents.SurfaceRangeChanged -> surfaceRangeChanged(events)
            is FilterEvents.BathSelectedChange -> bathSelectionChanged(events)
            is FilterEvents.LifecycleEvent -> handleLifecycle(events.event)
        }
        previewResults()
    }

    private fun handleLifecycle(event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModelScope.launch {
                val result = getFilter().first()
                (result as DataState.Data).data?.let {
                    _state.value = state.value.fromFilter(it)
                }
            }
        }
    }

    private fun onSaveFilter() {
        viewModelScope.launch {
            saveFilter(SaveFilterPreferencesUseCase.Request(state.value.toFilter())).collect {
                _state.value = state.value.copy(uiEvent = UiEvent.Navigate(Screen.Home.route))
            }
        }
    }

    private fun bathSelectionChanged(events: FilterEvents.BathSelectedChange) {
        val selectedBaths = state.value.selectedBaths.toMutableList()
        if (selectedBaths.contains(events.newValue)) {
            selectedBaths.remove(events.newValue)
        } else {
            selectedBaths.add(events.newValue)
        }
        _state.value = state.value.copy(selectedBaths = selectedBaths)
    }

    private fun surfaceRangeChanged(events: FilterEvents.SurfaceRangeChanged) {
        _state.value = state.value.copy(surfaceRange = events.range)
    }

    private fun dormsSelectionChanged(events: FilterEvents.DormsSelectedChange) {
        val selectedDorms = state.value.selectedDorms.toMutableList()
        if (selectedDorms.contains(events.newValue)) {
            selectedDorms.remove(events.newValue)
        } else {
            selectedDorms.add(events.newValue)
        }
        _state.value = state.value.copy(selectedDorms = selectedDorms)
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
                state.value.copy(previewResultCount = (single as DataState.Data).data!!.size)
        }
    }
}