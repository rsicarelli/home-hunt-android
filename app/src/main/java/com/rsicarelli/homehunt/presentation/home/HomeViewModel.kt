package com.rsicarelli.homehunt.presentation.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.usecase.GetPropertiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProperties: GetPropertiesUseCase
) : ViewModel() {

    private val _state: MutableState<HomeState> = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    fun onEvent(events: HomeEvents) {
        when (events) {
            is HomeEvents.LifecycleEvent -> getProperties(events)
        }
    }

    private fun getProperties(events: HomeEvents.LifecycleEvent) {
        if (events.event == Lifecycle.Event.ON_RESUME) {
            viewModelScope.launch {
                getProperties().onEach { dataState ->
                    println(dataState)
                    when (dataState) {
                        is DataState.Data -> state.updateProperties(dataState.data!!)
                        is DataState.Loading -> state.toggleLoading(dataState.progressBarState)
                    }
                }.launchIn(viewModelScope)
            }
        }
    }

    private fun State<HomeState>.updateProperties(properties: List<Property>) {
        _state.value = this.value.copy(
            properties = properties,
            emptyResults = properties.isEmpty(),
            progressBarState = ProgressBarState.Idle
        )
    }

    private fun State<HomeState>.toggleLoading(progressBarState: ProgressBarState) {
        _state.value = this.value.copy(progressBarState = progressBarState)
    }
}