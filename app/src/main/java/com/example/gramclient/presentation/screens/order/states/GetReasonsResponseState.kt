package com.example.gramclient.presentation.screens.order.states

import com.example.gramclient.domain.orderExecutionScreen.reason.GetReasonsResponse

data class GetReasonsResponseState(
    val isLoading: Boolean = false,
    var response: GetReasonsResponse? = null,
    val error: String = ""
)