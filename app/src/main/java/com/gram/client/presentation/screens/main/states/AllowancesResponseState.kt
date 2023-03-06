package com.gram.client.presentation.screens.main.states

import com.gram.client.domain.mainScreen.ToDesiredAllowance

data class AllowancesResponseState(
    val isLoading: Boolean = false,
    var response: List<ToDesiredAllowance>? = null,
    val error: String = ""
)
