package com.gram.client.domain.myAddresses

data class GetAllMyAddressesResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: GetAllMyAddressesResult,
    val success: Boolean
)