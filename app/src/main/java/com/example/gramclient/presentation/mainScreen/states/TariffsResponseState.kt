package com.example.gramclient.presentation.mainScreen.states

import com.example.gramclient.domain.mainScreen.TariffsResult

data class TariffsResponseState(
    val isLoading: Boolean = false,
    var response: List<TariffsResult>? = null,
    val error: String = ""
)
