package com.gram.client.domain.countries

data class GetCountriesKeyResponseState(
    val isLoading: Boolean = false,
    var response: ResultKey? = null,
    val error: String = "",
)