package com.gram.client.domain.myAddresses

data class AddMyAddressesResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: AddMyAddressesResult,
    val success: Boolean
)