package com.gram.client.domain.orderExecutionScreen

import com.gram.client.domain.orderExecutionScreen.active.AllActiveOrdersResult


data class ActiveOrdersResponseState(
    val isLoading: Boolean = false,
    var response:List<AllActiveOrdersResult>? = null,
    val error: String = "",
    val success: Boolean = false,
    val code:Int? = null
)