package com.gram.client.domain.mainScreen.fast_address

data class FastAddressesResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: List<Result>,
    val success: Boolean
)