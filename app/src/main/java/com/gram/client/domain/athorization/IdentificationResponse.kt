package com.gram.client.domain.athorization

data class IdentificationResponse(
    val code: Int,
    val error: List<Any>,
    val message: String,
    val result: IdentificationResult,
    val success: Boolean
)