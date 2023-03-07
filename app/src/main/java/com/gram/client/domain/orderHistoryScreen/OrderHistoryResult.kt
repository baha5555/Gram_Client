package com.gram.client.domain.orderHistoryScreen

data class OrderHistoryResult(
    val allowances: List<Allowance>,
    val comment: Any,
    val created_at: String,
    val dop_phone: Any,
    val from_address: FromAddress?,
    val id: Int,
    val performer: Performer,
    val phone: String,
    val price: Int,
    val search_address_id: Int,
    val status: String,
    val tariff: String,
    val tariff_id: Int,
    val to_addresses: List<ToAddresse>?
)