package com.example.gramclient.domain.orderHistory

data class Data(
    val allowances: List<Allowance>,
    val comment: Any,
    val created_at: String,
    val dop_phone: Any,
    val filing_time: String,
    val from_address: FromAddress?=null,
    val id: Int,
    val meeting_info: Any,
    val performer: Performer,
    val phone: String,
    val price: Int,
    val search_address_id: Int,
    val status: String,
    val tariff: String,
    val tariff_id: Int,
    val to_addresses: List<ToAddresse>?=null
)