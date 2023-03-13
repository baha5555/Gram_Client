package com.gram.client.domain.mainScreen.order

data class CalculateResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: List<PriceResult>,
    val success: Boolean
)