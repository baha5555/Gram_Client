package com.gram.client.presentation.screens.order.states

import com.gram.client.domain.orderExecutionScreen.reason.GetRatingReasonsResponse

data class GetRatingReasonsResponseState(
    val isLoading: Boolean = false,
    var response: GetRatingReasonsResponse? = null,
    val error: String = ""
)