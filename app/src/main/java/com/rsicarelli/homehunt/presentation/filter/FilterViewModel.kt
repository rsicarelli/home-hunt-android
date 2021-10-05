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
        }
    }
}