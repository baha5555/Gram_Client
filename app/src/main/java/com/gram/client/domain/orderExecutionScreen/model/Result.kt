package com.gram.client.domain.orderExecutionScreen.model

data class Result(
    val exception: Any,
    val headers: Headers,
    val original: Original
)