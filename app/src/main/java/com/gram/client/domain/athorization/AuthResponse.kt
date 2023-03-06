package com.gram.client.domain.athorization

data class AuthResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: Result,
    val success: Boolean
)