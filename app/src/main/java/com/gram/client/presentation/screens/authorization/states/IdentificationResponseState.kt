package com.gram.client.presentation.screens.authorization.states

import com.gram.client.domain.athorization.IdentificationResult

data class IdentificationResponseState(
    val isLoading: Boolean = false,
    var response: IdentificationResult? = null,
    val error: String = ""
)
