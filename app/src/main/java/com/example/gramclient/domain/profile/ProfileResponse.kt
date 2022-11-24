package com.example.gramclient.domain.profile

data class ProfileResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: Result,
    val success: Boolean
)