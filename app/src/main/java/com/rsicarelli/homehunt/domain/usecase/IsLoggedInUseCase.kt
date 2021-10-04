package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.domain.repository.UserRepository
import kotlinx.coroutines.flow.flow

class IsLoggedInUseCase(
    private val userRepository: UserRepository
) {

    operator fun invoke() = flow {
        emit(DataState.Data(userRepository.isLoggedIn()))
    }

}