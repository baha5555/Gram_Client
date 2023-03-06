package com.gram.client.domain.profile

data class ProfileResponse(
    val code: Int,
    val error: List<ProfileErrors>? = null,
    val message: String,
    val result: List<Result>? = null,
    val success: Boolean
)