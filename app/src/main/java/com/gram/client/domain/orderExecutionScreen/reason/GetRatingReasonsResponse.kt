package com.gram.client.domain.orderExecutionScreen.reason

data class GetRatingReasonsResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: List<Result>,
    val success: Boolean
)