package com.example.gramclient.presentation.screens.main.states

import com.example.gramclient.domain.mainScreen.order.CalculateResponse


data class CalculateResponseState(
    val isLoading: Boolean = false,
    var response: CalculateResponse? = null,
    val error: String = ""
)