package com.rsicarelli.homehunt.presentation.login

import com.google.firebase.auth.AuthCredential

sealed class LoginEvents {
    data class Login(val credential: AuthCredential) : LoginEvents()
    data class Error(val exception: Exception) : LoginEvents()
}