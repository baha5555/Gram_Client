package com.gram.client.domain.mainScreen.order.connectClientWithDriver

data class connectClientWithDriverResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: List<Any>,
    val success: Boolean
)