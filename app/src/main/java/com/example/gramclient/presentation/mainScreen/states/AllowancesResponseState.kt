package com.example.gramclient.presentation.mainScreen.states

import com.example.gramclient.domain.mainScreen.Allowance
import com.example.gramclient.domain.mainScreen.TariffsResult
import com.example.gramclient.domain.mainScreen.ToDesiredAllowance

data class AllowancesResponseState(
    val isLoading: Boolean = false,
    var response: List<ToDesiredAllowance>? = null,
    val error: String = ""
)
