package com.gram.client.domain.mainScreen

data class AddressByPointResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: Address,
    val success: Boolean
)