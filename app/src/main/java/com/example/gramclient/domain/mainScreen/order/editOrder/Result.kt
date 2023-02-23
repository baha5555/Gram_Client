package com.example.gramclient.domain.mainScreen.order.editOrder

data class Result(
    val allowances: List<Any>,
    val comment: Any,
    val created_at: String,
    val dop_phone: Any,
    val filing_time: String,
    val from_address: FromAddress,
    val id: Int,
    val meeting_info: Any,
    val performer: Any,
    val phone: String,
    val point_start: Any,
    val price: Int,
    val search_address_id: Int,
    val status: String,
    val tariff: String,
    val tariff_id: Int,
    val to_addresses: List<ToAddresse>
)