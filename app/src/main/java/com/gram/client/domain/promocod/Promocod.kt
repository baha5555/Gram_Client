package com.gram.client.domain.promocod

data class Promocod(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: Result,
    val success: Boolean
)