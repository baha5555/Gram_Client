package com.gram.client.presentation.screens.main.states

import com.gram.client.domain.mainScreen.Address
import com.gram.client.domain.mainScreen.AddressByPointResult


data class AddressByPointResponseState(
    val isLoading: Boolean = false,
    var response: Address? = null,
    val error: String = ""
)