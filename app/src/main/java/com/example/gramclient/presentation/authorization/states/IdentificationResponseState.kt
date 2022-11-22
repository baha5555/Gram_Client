package com.example.gramclient.presentation.authorization.states

import com.example.gramclient.domain.athorization.IdentificationResponse

data class IdentificationResponseState(
    val isLoading: Boolean = false,
    var response: IdentificationResponse? = null,
    val error: String = ""
)
