package com.rsicarelli.homehunt.domain.usecase

import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class IsLoggedInUseCase(
    private val userRepository: UserRepository
) {

    operator fun invoke() = flow {
        emit(DataState.Data(userRepository.isLoggedIn()))
    }.flowOn(Dispatchers.Default)

}