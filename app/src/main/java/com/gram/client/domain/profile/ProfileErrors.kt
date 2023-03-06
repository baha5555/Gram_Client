package com.gram.client.domain.profile

data class ProfileErrors(
    val first_name:List<String>? = null,
    val last_name:List<String>? = null,
    val email:List<String>? = null
)