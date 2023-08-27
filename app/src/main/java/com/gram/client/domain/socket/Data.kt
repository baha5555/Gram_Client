package com.gram.client.domain.socket

data class Data(
    val active_bonus: Int,
    val allowances: List<Any>,
    val bonus_price: Int,
    val client_status: Int,
    val comment: String,
    val create_order: String,
    val date_time: String,
    val distance: Int,
    val division: String,
    val dop_phone: String,
    val filing_time: Any,
    val for_time: Int,
    val from_address: FromAddress,
    val id: Int,
    val is_assigned: Int,
    val meeting_info: String,
    val performer: Any,
    val price: Int,
    val price_cash: Int,
    val status: String,
    val status_id: Int,
    val tariff: String,
    val tariff_id: Int,
    val to_address: List<Any>
)