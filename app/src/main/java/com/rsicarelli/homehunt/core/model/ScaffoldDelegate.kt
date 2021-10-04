package com.rsicarelli.homehunt.core.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ScaffoldDelegate(private val coroutineScope: CoroutineScope) {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val uiEvents = _eventFlow.asSharedFlow()

    fun handleUiState(uiEvent: UiEvent) {
        coroutineScope.launch { _eventFlow.emit(uiEvent) }
    }
}