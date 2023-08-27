package com.gram.client.domain.mainScreen.order

data class Original(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: Int,
    val success: Boolean
)