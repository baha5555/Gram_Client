package com.gram.client.presentation.screens.main.states

import com.gram.client.domain.mainScreen.fast_address.Result

data class FastAddressesResponseState(
    val isLoading: Boolean = false,
    var response: List<Result>? = null,
    val error: String = ""
)