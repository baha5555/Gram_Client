package com.example.gramclient.domain.orderHistory

data class OrderHistoryPagingResult(
    val cade: Int,
    val error: List<Any>,
    val message: String,
    val result: Result,
    val success: Boolean
)