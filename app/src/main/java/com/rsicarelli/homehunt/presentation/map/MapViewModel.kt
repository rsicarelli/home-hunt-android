package com.rsicarelli.homehunt.presentation.map

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.R
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.core.model.UiText
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.usecase.GetFilteredPropertiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase,
) : ViewModel() {

    private val _state: MutableState<MapState> = mutableStateOf(MapState())
    val state: State<MapState> = _state

    init {
        viewModelScope.launch {
            getFilteredPropertiesUseCase(Unit)
                .onStart { toggleLoading(ProgressBarState.Loading) }
                .onEach { outcome ->
                    updateProperties(outcome.properties)
                        .also { toggleLoading(ProgressBarState.Idle) }
                }
                .catch { showError().also { Timber.e("Error trying to get properties", it) } }
                .launchIn(viewModelScope)
        }
    }

    private fun updateProperties(properties: List<Property>) {
        _state.value = state.value.copy(properties = properties)
    }

    private fun toggleLoading(progressBarState: ProgressBarState) {
        _state.value = state.value.copy(progressBarState = progressBarState)
    }

    private fun showError() {
        _state.value =
            state.value.copy(uiEvent = UiEvent.MessageToUser(UiText.StringResource(R.string.error_loading_properties)))
    }
}
