package com.gram.client.domain.profile

data class Result(
    val avatar: String,
    val balance: Balance,
    val birth_date: Any,
    val email: String,
    val first_name: String,
    val gender: Any,
    val id: Int,
    val last_name: String,
    val login: String,
    val phone: String,
    val user_id: Int
)