package com.example.gramclient.presentation.screens.main.states

import com.example.gramclient.domain.mainScreen.Address

data class SearchAddressResponseState(
    val isLoading: Boolean = false,
    var response: List<Address>? = null,
    val error: String = ""
)