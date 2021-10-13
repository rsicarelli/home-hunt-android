package com.rsicarelli.homehunt.presentation.map

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.domain.usecase.GetFilteredPropertiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase,
) : ViewModel() {

    private val _state: MutableState<MapState> = mutableStateOf(MapState())
    val state: State<MapState> = _state

    init {
        viewModelScope.launch {
            getFilteredPropertiesUseCase().distinctUntilChanged().onEach { dataState ->
                when (dataState) {
                    is DataState.Data -> _state.value =
                        state.value.copy(properties = dataState.data!!)
                }
            }.launchIn(viewModelScope)
        }
    }
}
