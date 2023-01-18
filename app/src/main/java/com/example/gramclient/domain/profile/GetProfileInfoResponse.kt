package com.example.gramclient.domain.profile

data class GetProfileInfoResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: GetProfileInfoResult,
    val success: Boolean
)