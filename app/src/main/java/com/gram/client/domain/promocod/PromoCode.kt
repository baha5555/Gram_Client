package com.gram.client.domain.promocod

data class PromoCode(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: Result,
    val success: Boolean
)