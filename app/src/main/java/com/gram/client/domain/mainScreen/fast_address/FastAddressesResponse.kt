package com.gram.client.domain.mainScreen.fast_address

import com.gram.client.domain.mainScreen.Address

data class FastAddressesResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: List<Address>,
    val success: Boolean
)