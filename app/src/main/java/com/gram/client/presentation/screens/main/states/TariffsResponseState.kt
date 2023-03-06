package com.gram.client.presentation.screens.main.states

import com.gram.client.domain.mainScreen.TariffsResult

data class TariffsResponseState(
    val isLoading: Boolean = false,
    var response: List<TariffsResult>? = null,
    val error: String = ""
)
