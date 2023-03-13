package com.gram.client.domain.promocod

 data class GetPromocodResponse (
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: Message,
    val success: Boolean
)