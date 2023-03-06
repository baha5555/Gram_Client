package com.gram.client.domain.orderExecutionScreen

import com.gram.client.domain.mainScreen.order.UpdateOrderResponse


data class EditOrderResponseState(
    val isLoading: Boolean = false,
    var response: UpdateOrderResponse? = null,
    val error: String = ""
)