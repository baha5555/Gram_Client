package com.gram.client.domain.mainScreen

data class TariffsResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: List<TariffsResult>,
    val success: Boolean
)