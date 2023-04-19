package com.gram.client.domain.myAddresses

data class MyAddressSendModel(
    val id: Int,
    val name: String,
    val search_address_id: Int,
    val meet_info: String?,
    val comment_to_driver: String?,
    val type: String
)

data class MyAddAddressSendModel(
    val name: String,
    val search_address_id: Int,
    val meet_info: String?,
    val comment_to_driver: String?,
    val type: String
)
