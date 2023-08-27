package com.gram.client.domain.mainScreen.order

import com.gram.client.domain.mainScreen.order.editOrder.Result

data class OrderResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: com.gram.client.domain.mainScreen.order.Result,
    val success: Boolean
)

data class UpdateOrderResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: Result,
    val success: Boolean
)