package com.gram.client.presentation.screens.order.states

import com.gram.client.domain.orderExecutionScreen.reason.Reasons

data class GetReasonsResponseState(
    val isLoading: Boolean = false,
    var response: Reasons? = null,
    val error: String = ""
)