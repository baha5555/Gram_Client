package com.example.gramclient.presentation.screens.main.states

import com.example.gramclient.domain.mainScreen.fast_address.Result

data class FastAddressesResponseState(
    val isLoading: Boolean = false,
    var response: List<Result>? = null,
    val error: String = ""
)