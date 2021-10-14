package com.rsicarelli.homehunt.presentation.home

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.usecase.GetFilteredPropertiesUseCase
import com.rsicarelli.homehunt.domain.usecase.ToggleFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase,
    private val toggleFavourite: ToggleFavouriteUseCase,
) : ViewModel() {

    private var job: Job? = null
    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }

    fun onEvent(events: HomeEvents) {
        when (events) {
            is HomeEvents.LifecycleEvent -> getProperties(events)
            is HomeEvents.ToggleFavourite -> toggleFavourite(
                request = ToggleFavouriteUseCase.Request(
                    events.reference,
                    events.isFavourited
                )
            )
            HomeEvents.GetProperties -> getProperties()
        }
    }

    private fun getProperties() {
        viewModelScope.launch {
            getFilteredPropertiesUseCase().distinctUntilChanged().onEach { dataState ->
                println(dataState)
                when (dataState) {
                    is DataState.Data -> updateProperties(dataState.data!!)
                    is DataState.Loading -> toggleLoading(dataState.progressBarState)
                    is DataState.Error -> Timber.e("Something wrong is not right")
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun getProperties(events: HomeEvents.LifecycleEvent) {
        if (events.event == Lifecycle.Event.ON_RESUME) {
            viewModelScope.launch {
                job =
                    getFilteredPropertiesUseCase().distinctUntilChanged().onEach { dataState ->
                        println(dataState)
                        when (dataState) {
                            is DataState.Data -> updateProperties(dataState.data!!)
                            is DataState.Loading -> toggleLoading(dataState.progressBarState)
                            is DataState.Error -> Timber.e("Something wrong is not right")
                        }
                    }.launchIn(viewModelScope)
            }
        }
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
}