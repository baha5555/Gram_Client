package com.gram.client.domain.myAddresses

import com.gram.client.domain.mainScreen.Address

data class Work(
    val address: Address,
    val client_id: Int,
    val comment_to_driver: String,
    val id: Int,
    val meet_info: String,
    val name: String,
    val type: String
)