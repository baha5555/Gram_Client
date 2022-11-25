package com.example.gramclient.domain.profile

data class GetProfileInfoResponseState(
    val isLoading: Boolean = false,
    var response: ResultX? = null,
    val error: String = ""
)
