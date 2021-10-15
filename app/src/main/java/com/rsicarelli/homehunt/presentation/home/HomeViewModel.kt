package com.rsicarelli.homehunt.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.domain.usecase.GetFilteredPropertiesUseCase
import com.rsicarelli.homehunt.domain.usecase.ToggleFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase,
    private val toggleFavourite: ToggleFavouriteUseCase,
) : ViewModel() {

    private val state: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())

    fun init() = state.also { loadProperties() }

    private fun loadProperties() {
        getFilteredPropertiesUseCase.invoke(Unit)
            .onEach {
                state.value = state.value.copy(
                    properties = it.properties,
                    progressBarState = ProgressBarState.Idle,
                    isEmpty = it.properties.isEmpty()
                )
            }.launchIn(viewModelScope)
    }

    fun toggleFavourite(referenceId: String, isFavourited: Boolean) {
        viewModelScope.launch {
            toggleFavourite(
                request = ToggleFavouriteUseCase.Request(
                    referenceId,
                    isFavourited
                )
            ).single()
        }
    }

}