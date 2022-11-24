package com.example.gramclient.domain.mainScreen

data class AddressByPointResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: AddressByPointResult,
    val success: Boolean
)