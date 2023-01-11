package com.example.gramclient.presentation.screens.main.states

import com.example.gramclient.domain.mainScreen.ToDesiredAllowance

data class AllowancesResponseState(
    val isLoading: Boolean = false,
    var response: List<ToDesiredAllowance>? = null,
    val error: String = ""
)
