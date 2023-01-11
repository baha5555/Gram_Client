package com.example.gramclient.presentation.screens.main.states

import com.example.gramclient.domain.mainScreen.AddressByPointResult


data class AddressByPointResponseState(
    val isLoading: Boolean = false,
    var response: AddressByPointResult? = null,
    val error: String = ""
)