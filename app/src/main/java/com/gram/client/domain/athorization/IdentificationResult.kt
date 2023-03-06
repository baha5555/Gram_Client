package com.gram.client.domain.athorization

data class IdentificationResult(
    val access_token: String,
    val expires_at: String,
    val token_type: String
)