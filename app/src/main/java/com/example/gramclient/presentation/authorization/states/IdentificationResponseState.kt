package com.example.gramclient.presentation.authorization.states

import com.example.gramclient.domain.athorization.IdentificationResult

data class IdentificationResponseState(
    val isLoading: Boolean = false,
    var response: IdentificationResult? = null,
    val error: String = ""
)
