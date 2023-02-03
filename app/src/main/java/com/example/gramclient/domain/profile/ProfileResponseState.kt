package com.example.gramclient.domain.profile

import com.example.gramclient.domain.mainScreen.Allowance

data class ProfileResponseState(
    val isLoading: Boolean = false,
    var response: Result? = null,
    val error: List<ProfileErrors>?= null
)
