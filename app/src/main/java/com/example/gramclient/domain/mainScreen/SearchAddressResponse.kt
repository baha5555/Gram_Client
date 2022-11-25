package com.example.gramclient.domain.mainScreen

data class SearchAddressResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: List<Address>,
    val success: Boolean
)