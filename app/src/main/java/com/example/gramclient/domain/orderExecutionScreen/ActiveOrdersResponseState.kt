package com.example.gramclient.domain.orderExecutionScreen


data class ActiveOrdersResponseState(
    val isLoading: Boolean = false,
    var response: List<Order>? = null,
    val error: String = ""
)