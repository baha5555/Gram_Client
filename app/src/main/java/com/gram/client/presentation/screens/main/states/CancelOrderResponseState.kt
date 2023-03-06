package com.gram.client.presentation.screens.main.states

import com.gram.client.domain.mainScreen.order.CancelOrderResponse


data class CancelOrderResponseState(
    val isLoading: Boolean = false,
    var response: CancelOrderResponse? = null,
    val error: String = ""
)