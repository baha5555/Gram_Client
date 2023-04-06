package com.gram.client.domain.orderExecutionScreen.reason

data class Reasons(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: List<GetReasonsResponseItem>,
    val success: Boolean
)