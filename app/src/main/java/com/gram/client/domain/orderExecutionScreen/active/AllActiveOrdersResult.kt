package com.gram.client.domain.orderExecutionScreen.active

import com.gram.client.domain.mainScreen.Address

data class AllActiveOrdersResult(
    val active_bonus: Int? = 0,
    val allowances: List<Allowance>? = null,
    val bonus_price: Int? = 0,
    val comment: String? = "",
    val created_at: String?= "",
    val distance: Double? = 0.0,
    val dop_phone: String? = "",
    val filing_time: String?= "",
    val filing_time_to_int: Int?=0,
    val from_address: Address?= null,
    val id: Int = 0,
    val meeting_info: String? = "",
    val performer: Performer?= null,
    val phone: String? = "",
    val price: Double? = 0.0,
    val price_cash: Double? = 0.0,
    val search_address_id: Int? = 0,
    val status: String = "",
    val tariff: String? = "",
    val tariff_id: Int = 0,
    val to_addresses: List<Address>?= null
)