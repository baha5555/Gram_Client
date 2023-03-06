package com.gram.client.presentation.screens.main.states

import com.gram.client.domain.mainScreen.order.CalculateResponse


data class CalculateResponseState(
    val isLoading: Boolean = false,
    var response: CalculateResponse? = null,
    val error: String = ""
)