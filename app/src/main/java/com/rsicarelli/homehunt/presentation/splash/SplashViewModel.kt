package com.rsicarelli.homehunt.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rsicarelli.homehunt.ui.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<Screen>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {

            delay(100)
            _eventFlow.emit(Screen.LoginScreen)
//            when (authenticateUseCase()) {
//                is Resource.Success -> {
//                    _eventFlow.emit(
//                        UiEvent.Navigate(Screen.MainFeedScreen.route)
//                    )
//                }
//                is Resource.Error -> {
//                    _eventFlow.emit(
//                        UiEvent.Navigate(Screen.LoginScreen.route)
//                    )
//                }
//            }
        }
    }
}