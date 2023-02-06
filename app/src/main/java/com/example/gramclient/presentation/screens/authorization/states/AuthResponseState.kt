package com.example.gramclient.presentation.screens.authorization.states

import com.example.gramclient.domain.athorization.AuthResponse


data class AuthResponseState(
    val isLoading: Boolean = false,
    var response: AuthResponse? = null,
    val error: String = ""
)