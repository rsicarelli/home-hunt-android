package com.rsicarelli.homehunt.presentation.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.domain.model.Property
import com.rsicarelli.homehunt.domain.usecase.GetFilteredPropertiesUseCase
import com.rsicarelli.homehunt.domain.usecase.MarkAsViewedUseCase
import com.rsicarelli.homehunt.domain.usecase.ToggleFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import javax.inject.Inject

//TODO the same as HomeViewModel, unify somehow
@HiltViewModel
class MapViewModel @Inject constructor(
    private val getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase,
    private val toggleFavourite: ToggleFavouriteUseCase,
    private val markAsViewed: MarkAsViewedUseCase,
) : ViewModel() {

    private val state: MutableStateFlow<MapState> = MutableStateFlow(MapState())

    fun init() = state.also { loadProperties() }

    private fun loadProperties() {
        getFilteredPropertiesUseCase.invoke(Unit)
            .onEach {
                state.value = state.value.copy(
                    properties = it.properties,
                    progressBarState = ProgressBarState.Idle,
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

    fun onPropertyViewed(property: Property) {
        if (property.viewedByMe()) return

        viewModelScope.launch {
            markAsViewed.invoke(MarkAsViewedUseCase.Request(property.reference)).single()
        }
    }

    fun onMarkerSelected(property: Property) {
        state.value = state.value.copy(propertySnippet = property)
    }

    fun onMapClicked() {
        state.value = state.value.copy(propertySnippet = null)
    }
}
