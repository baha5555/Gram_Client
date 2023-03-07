package com.gram.client.domain.mainScreen.order

data class CancelOrderResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: List<CancelOrderResult>,
    val success: Boolean
)
data class CancelOrderResult(
    val count: Int
)