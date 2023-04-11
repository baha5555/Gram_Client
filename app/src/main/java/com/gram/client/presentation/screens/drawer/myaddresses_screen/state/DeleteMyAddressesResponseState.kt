package com.gram.client.presentation.screens.drawer.myaddresses_screen.state

import com.gram.client.domain.athorization.AuthResponse
import com.gram.client.domain.myAddresses.AddMyAddressesResponse
import com.gram.client.domain.myAddresses.DeleteMyAddressesResponse
import com.gram.client.domain.myAddresses.UpdateMyAddressResponse


data class DeleteMyAddressesResponseState(
    val isLoading: Boolean = false,
    var response: DeleteMyAddressesResponse? = null,
    val error: String = ""
)