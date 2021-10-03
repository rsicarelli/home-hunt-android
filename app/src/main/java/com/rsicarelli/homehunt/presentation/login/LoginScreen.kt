package com.rsicarelli.homehunt.presentation.login

import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    scaffoldState: ScaffoldState,
    onNavigate: (String) -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    return Text(text = "Login")
}