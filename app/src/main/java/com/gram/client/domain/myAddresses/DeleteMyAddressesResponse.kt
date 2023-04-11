package com.gram.client.domain.myAddresses

data class DeleteMyAddressesResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: Boolean,
    val success: Boolean
)