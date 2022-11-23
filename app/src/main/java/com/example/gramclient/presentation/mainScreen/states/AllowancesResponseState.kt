package com.example.gramclient.presentation.mainScreen.states

import com.example.gramclient.domain.mainScreen.Allowance
import com.example.gramclient.domain.mainScreen.TariffsResult

data class AllowancesResponseState(
    val isLoading: Boolean = false,
    var response: List<Allowance>? = null,
    val error: String = ""
)
