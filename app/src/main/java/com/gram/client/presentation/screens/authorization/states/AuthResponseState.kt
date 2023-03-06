package com.gram.client.presentation.screens.authorization.states

import com.gram.client.domain.athorization.AuthResponse


data class AuthResponseState(
    val isLoading: Boolean = false,
    var response: AuthResponse? = null,
    val error: String = ""
)