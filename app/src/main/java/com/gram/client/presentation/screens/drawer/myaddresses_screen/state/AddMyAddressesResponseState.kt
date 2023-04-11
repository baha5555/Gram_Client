package com.gram.client.presentation.screens.drawer.myaddresses_screen.state

import com.gram.client.domain.athorization.AuthResponse
import com.gram.client.domain.myAddresses.AddMyAddressesResponse


data class AddMyAddressesResponseState(
    val isLoading: Boolean = false,
    var response: AddMyAddressesResponse? = null,
    val error: String = ""
)