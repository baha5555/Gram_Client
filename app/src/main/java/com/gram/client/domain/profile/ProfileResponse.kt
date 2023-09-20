package com.gram.client.domain.profile

data class ProfileResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: List<Result>,
    val success: Boolean
)