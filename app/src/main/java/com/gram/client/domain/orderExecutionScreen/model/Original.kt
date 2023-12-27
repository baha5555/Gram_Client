package com.gram.client.domain.orderExecutionScreen.model

data class Original(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: ResultX,
    val success: Boolean
)