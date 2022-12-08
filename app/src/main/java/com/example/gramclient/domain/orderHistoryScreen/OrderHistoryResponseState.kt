package com.example.gramclient.domain.orderHistoryScreen


data class OrderHistoryResponseState(
    val isLoading: Boolean = false,
    var response: List<OrderHistoryResult>? = null,
    val error: String = ""
)
