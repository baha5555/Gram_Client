package com.example.gramclient.presentation.mainScreen.states

import com.example.gramclient.domain.mainScreen.order.CancelOrderResponse


data class CancelOrderResponseState(
    val isLoading: Boolean = false,
    var response: CancelOrderResponse? = null,
    val error: String = ""
)