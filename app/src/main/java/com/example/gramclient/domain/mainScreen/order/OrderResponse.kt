package com.example.gramclient.domain.mainScreen.order

data class OrderResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: Int,
    val success: Boolean
)