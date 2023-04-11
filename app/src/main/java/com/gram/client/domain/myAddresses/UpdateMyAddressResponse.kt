package com.gram.client.domain.myAddresses

data class UpdateMyAddressResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: UpdateMyAddressResult,
    val success: Boolean
)