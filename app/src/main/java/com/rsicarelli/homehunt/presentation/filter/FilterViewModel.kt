package com.rsicarelli.homehunt.presentation.filter

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor() : ViewModel() {


    private val _state: MutableState<FilterState> = mutableStateOf(FilterState())
    val state: State<FilterState> = _state

    fun onEvent(events: FilterEvents) {
        when (events) {
            FilterEvents.ClearFilter -> TODO()
            FilterEvents.SaveFilter -> TODO()
            is FilterEvents.PriceRangeChanged -> {
                _state.value = state.value.copy(priceRange = events.range)
            }
            is FilterEvents.DormsSelectedChange -> {
                val selectedDorms = state.value.selectedDorms.toMutableList()
                if (selectedDorms.contains(events.newValue)) {
                    selectedDorms.remove(events.newValue)
                } else {
                    selectedDorms.add(events.newValue)
                }
                _state.value = state.value.copy(selectedDorms = selectedDorms)
            }
            is FilterEvents.SurfaceRangeChanged -> {
                _state.value = state.value.copy(surfaceRange = events.range)
            }
            is FilterEvents.BathSelectedChange -> {
                val selectedBaths = state.value.selectedBaths.toMutableList()
                if (selectedBaths.contains(events.newValue)) {
                    selectedBaths.remove(events.newValue)
                } else {
                    selectedBaths.add(events.newValue)
                }
                _state.value = state.value.copy(selectedBaths = selectedBaths)
            }
        }
    }
}