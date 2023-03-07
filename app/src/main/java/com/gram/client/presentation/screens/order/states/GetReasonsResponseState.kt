package com.gram.client.presentation.screens.order.states

import com.gram.client.domain.orderExecutionScreen.reason.GetReasonsResponse

data class GetReasonsResponseState(
    val isLoading: Boolean = false,
    var response: GetReasonsResponse? = null,
    val error: String = ""
)