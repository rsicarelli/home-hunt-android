package com.rsicarelli.homehunt.domain.usecase

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.rsicarelli.homehunt.core.model.DataState
import com.rsicarelli.homehunt.core.model.ProgressBarState
import com.rsicarelli.homehunt.core.model.UiEvent
import com.rsicarelli.homehunt.core.model.UiText
import com.rsicarelli.homehunt.core.util.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SignInUseCase(
    private val firebaseAuth: FirebaseAuth
) {
    operator fun invoke(request: Request) = flow<DataState<FirebaseUser>> {
        try {
            emit(DataState.Loading(ProgressBarState.Loading))

            val result = firebaseAuth.signInWithCredential(request.authCredential).await()

            if (result == null || result.user == null) {
                error("Invalid result $result, ${result?.user}")
            }

            emit(DataState.Data(result.user!!))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DataState.Error(UiEvent.MessageToUser(UiText.unknownError())))
        } finally {
            delay(100)
            emit(DataState.Loading(ProgressBarState.Idle))
        }
    }.flowOn(Dispatchers.IO)


    data class Request(
        val authCredential: AuthCredential
    )
}