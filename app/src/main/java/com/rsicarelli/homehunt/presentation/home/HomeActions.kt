package com.rsicarelli.homehunt.presentation.home

internal data class HomeActions(
    val onToggleFavourite: (String, Boolean) -> Unit,
    val onNavigate: (String) -> Unit,
)