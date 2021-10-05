package com.rsicarelli.homehunt.presentation.filter

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.domain.usecase.PreviewFilterResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val previewFilterResult: PreviewFilterResultUseCase
) : ViewModel() {

    private val _state: MutableState<FilterState> = mutableStateOf(FilterState())
    val state: State<FilterState> = _state

    fun onEvent(events: FilterEvents) {
        when (events) {
            FilterEvents.ClearFilter -> TODO()
            FilterEvents.SaveFilter -> TODO()
            is FilterEvents.PriceRangeChanged -> {
                println(events.range)
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
        previewResults()
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