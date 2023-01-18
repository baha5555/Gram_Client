package com.example.gramclient.domain.mainScreen.order

data class CancelOrderResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: List<Any>,
    val success: Boolean
)