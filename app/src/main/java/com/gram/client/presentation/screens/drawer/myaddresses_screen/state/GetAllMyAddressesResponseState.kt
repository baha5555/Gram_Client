package com.gram.client.presentation.screens.drawer.myaddresses_screen.state

import com.gram.client.domain.athorization.AuthResponse
import com.gram.client.domain.myAddresses.AddMyAddressesResponse
import com.gram.client.domain.myAddresses.GetAllMyAddressesResponse


data class GetAllMyAddressesResponseState(
    val isLoading: Boolean = false,
    var response: GetAllMyAddressesResponse? = null,
    val error: String = ""
)