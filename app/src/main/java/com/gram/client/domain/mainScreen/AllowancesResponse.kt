package com.gram.client.domain.mainScreen

data class AllowancesResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: List<Allowance>,
    val success: Boolean
)