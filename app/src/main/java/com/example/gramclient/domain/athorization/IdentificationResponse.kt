package com.example.gramclient.domain.athorization

data class IdentificationResponse(
    val code: Int,
    val message: String,
    val result: ResultIdentification,
    val success: Boolean
)