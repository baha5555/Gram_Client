package com.example.gramclient.domain.orderExecutionScreen

data class ActiveOrdersResponse(
    val code: Int,
    val message: String,
    val result: List<Order>,
    val success: Boolean
)