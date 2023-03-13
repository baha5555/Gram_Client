package com.gram.client.domain.promocod

import com.gram.client.domain.profile.GetProfileInfoResult

data class GetPromocodResponseState(
    val isLoading: Boolean = false,
    var response: Message? = null,
    val error: String = "",
)
