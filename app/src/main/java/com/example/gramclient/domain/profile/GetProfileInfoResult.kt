package com.example.gramclient.domain.profile

data class GetProfileInfoResult(
    val avatar_url: String? ="",
    val birth_date: String,
    val email: String,
    val first_name: String,
    val gender: Int,
    val id: Int,
    val last_name: String,
    val login: String,
    val phone: String,
    val user_id: Int
)