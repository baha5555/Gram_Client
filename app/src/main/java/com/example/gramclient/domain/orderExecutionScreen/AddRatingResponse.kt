package com.example.gramclient.domain.orderExecutionScreen

data class AddRatingResponse (
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: List<Any>,
    val success: Boolean
)