package com.example.gramclient.domain.orderHistoryScreen

data class orderHistoryResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: List<OrderHistoryResult>,
    val success: Boolean
)