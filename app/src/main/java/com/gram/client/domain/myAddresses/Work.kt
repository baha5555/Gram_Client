package com.gram.client.domain.myAddresses

data class Work(
    val client_id: Int,
    val comment_to_driver: String,
    val created_at: String,
    val id: Int,
    val meet_info: String,
    val name: String,
    val search_address_id: Int,
    val type: String,
    val updated_at: String
)