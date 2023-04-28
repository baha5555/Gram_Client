package com.gram.client.domain.countries

data class CountriesKeyResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: ResultKey,
    val success: Boolean
)