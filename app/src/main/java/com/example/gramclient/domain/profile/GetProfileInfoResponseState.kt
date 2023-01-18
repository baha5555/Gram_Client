package com.example.gramclient.domain.profile

data class GetProfileInfoResponseState(
    val isLoading: Boolean = false,
    var response: GetProfileInfoResult? = null,
    val error: String = ""
)
