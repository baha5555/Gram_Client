package com.gram.client.presentation.screens.main.states

import com.gram.client.domain.mainScreen.Address
import com.gram.client.domain.mainScreen.fast_address.Result

data class FastAddressesResponseState(
    val isLoading: Boolean = false,
    var response: List<Address>? = null,
    val error: String = ""
)