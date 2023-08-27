package com.gram.client.domain.orderExecutionScreen.active

data class ActiveOrdersResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: List<AllActiveOrdersResult>,
    val success: Boolean
)