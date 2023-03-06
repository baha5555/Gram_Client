package com.gram.client.presentation.screens.main.states

import com.gram.client.domain.mainScreen.Address

data class SearchAddressResponseState(
    val isLoading: Boolean = false,
    var response: List<Address>? = null,
    val error: String = ""
)