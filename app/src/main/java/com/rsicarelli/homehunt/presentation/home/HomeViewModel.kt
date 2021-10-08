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
import com.rsicarelli.homehunt.domain.usecase.GetFilteredPropertiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase
) : ViewModel() {

    private var job: Job? = null
    private val _state: MutableState<HomeState> = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    init {

    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }

    fun onEvent(events: HomeEvents) {
        when (events) {
            is HomeEvents.LifecycleEvent -> getProperties(events)
        }
    }

    private fun getProperties(events: HomeEvents.LifecycleEvent) {
        if (events.
            event == Lifecycle.Event.ON_RESUME) {
            viewModelScope.launch {
                job =
                    getFilteredPropertiesUseCase().distinctUntilChanged().onEach { dataState ->
                        println(dataState)
                        when (dataState) {
                            is DataState.Data -> updateProperties(dataState.data!!)
                            is DataState.Loading -> state.toggleLoading(dataState.progressBarState)
                            is DataState.Error -> Timber.e("Something wrong is not right")
                        }
                    }.launchIn(viewModelScope)
            }
        }
    }

    private fun updateProperties(properties: List<Property>) {
        val copy = state.value.copy(
            properties = properties,
            emptyResults = properties.isEmpty(),
            progressBarState = ProgressBarState.Idle
        )
        _state.value = copy
    }

    private fun State<HomeState>.toggleLoading(progressBarState: ProgressBarState) {
        _state.value = this.value.copy(progressBarState = progressBarState)
    }
}