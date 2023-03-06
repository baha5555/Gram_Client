package com.gram.client.domain.orderExecutionScreen

data class AddRatingResponseState(
    val isLoading: Boolean = false,
    var response: List<Any>? = null,
    val error: String = ""
)
