package com.example.gramclient.domain.mainScreen.order

data class CalculateResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: PriceResult,
    val success: Boolean
)