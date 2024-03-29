package com.gram.client.domain.profile

data class GetProfileInfoResponseState(
    val isLoading: Boolean = false,
    var response: GetProfileInfoResult? = null,
    val error: String = "",
    val success: Boolean = false)
