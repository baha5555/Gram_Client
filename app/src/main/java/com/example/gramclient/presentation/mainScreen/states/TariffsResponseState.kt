package com.example.gramclient.presentation.mainScreen.states

import com.example.gramclient.domain.TariffsResponse
import com.example.gramclient.domain.TariffsResult
import com.example.gramclient.domain.athorization.IdentificationResponse

data class TariffsResponseState(
    val isLoading: Boolean = false,
    var response: List<TariffsResult>? = null,
    val error: String = ""
)
