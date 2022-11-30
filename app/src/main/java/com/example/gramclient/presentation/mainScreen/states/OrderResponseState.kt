package com.example.gramclient.presentation.mainScreen.states

import com.example.gramclient.domain.mainScreen.order.OrderResponse


data class OrderResponseState(
    val isLoading: Boolean = false,
    var response: OrderResponse? = null,
    val error: String = ""
)