package com.example.gramclient.domain.athorization

data class ResultIdentification(
    val access_token: String,
    val expires_at: String,
    val token_type: String
)