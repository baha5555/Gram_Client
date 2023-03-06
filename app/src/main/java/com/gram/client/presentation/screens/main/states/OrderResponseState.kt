package com.gram.client.presentation.screens.main.states

import com.gram.client.domain.mainScreen.order.OrderResponse


data class OrderResponseState(
    val isLoading: Boolean = false,
    var response: OrderResponse? = null,
    val error: String = ""
)