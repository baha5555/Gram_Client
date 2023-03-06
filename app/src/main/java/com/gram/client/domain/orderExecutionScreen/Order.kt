package com.gram.client.domain.orderExecutionScreen

import com.gram.client.domain.orderHistoryScreen.FromAddress
import com.gram.client.domain.orderHistoryScreen.Performer
import com.gram.client.domain.orderHistoryScreen.ToAddresse

data class Order(
    val allowances: List<Any>,
    val comment: Any,
    val created_at: String,
    val dop_phone: Any,
    val from_address: FromAddress?,
    val id: Int,
    val meeting_info: Any,
    val performer: Performer?,
    val phone: String,
    val price: Int,
    val search_address_id: Any,
    val status: String,
    val tariff: String,
    val tariff_id: Int,
    val to_addresses: List<ToAddresse>?
)
