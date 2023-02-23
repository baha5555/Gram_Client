package com.example.gramclient.domain.mainScreen.order

import com.example.gramclient.domain.mainScreen.order.editOrder.Result

data class OrderResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: Int,
    val success: Boolean
)

data class UpdateOrderResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: Result,
    val success: Boolean
)