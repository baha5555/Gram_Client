package com.gram.client.domain.profile

data class GetProfileInfoResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: GetProfileInfoResult,
    val success: Boolean
)