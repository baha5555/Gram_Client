package com.example.gramclient.domain.orderHistory

data class Character(
    val cade: Int,
    val error: List<Any>,
    val message: String,
    val result: List<Result> = listOf(),
    val success: Boolean
)