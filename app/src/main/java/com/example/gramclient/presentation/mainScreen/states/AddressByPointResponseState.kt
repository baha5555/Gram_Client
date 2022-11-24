package com.example.gramclient.presentation.mainScreen.states

import com.example.gramclient.domain.mainScreen.AddressByPointResult
import com.example.gramclient.domain.mainScreen.Allowance


data class AddressByPointResponseState(
    val isLoading: Boolean = false,
    var response: AddressByPointResult? = null,
    val error: String = ""
)