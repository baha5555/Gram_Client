package com.gram.client.domain.promocod

data class GetPromocodResponseState(
    val isLoading: Boolean = false,
    var response: Result? = null,
    val error: String = "",
    
)
