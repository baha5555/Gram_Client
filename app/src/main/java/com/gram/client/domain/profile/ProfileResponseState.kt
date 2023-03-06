package com.gram.client.domain.profile

data class ProfileResponseState(
    val isLoading: Boolean = false,
    var response: Result? = null,
    val error: List<ProfileErrors>?= null
)
