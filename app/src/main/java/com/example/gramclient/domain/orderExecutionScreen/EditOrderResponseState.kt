package com.example.gramclient.domain.orderExecutionScreen

import com.example.gramclient.domain.mainScreen.order.OrderResponse
import com.example.gramclient.domain.mainScreen.order.UpdateOrderResponse


data class EditOrderResponseState(
    val isLoading: Boolean = false,
    var response: UpdateOrderResponse? = null,
    val error: String = ""
)