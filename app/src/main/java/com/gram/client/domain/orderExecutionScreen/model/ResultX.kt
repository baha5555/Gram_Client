package com.gram.client.domain.orderExecutionScreen.model

import com.gram.client.domain.mainScreen.Address
import com.gram.client.domain.orderExecutionScreen.active.Allowance

data class ResultX(
    val active_bonus: Int,
    val allowances: List<Allowance>,
    val bonus_price: Int,
    val comment: Any,
    val commission_price: Int,
    val created_at: String,
    val distance: Double,
    val dop_phone: Any,
    val filing_time: Any,
    val filing_time_to_int: Any,
    val from_address: Any,
    val id: Int,
    val meeting_info: Any,
    val performer: Any,
    val phone: String,
    val price: Int,
    val price_cash: Int,
    val search_address_id: Int,
    val status: String,
    val tariff: String,
    val tariff_id: Int,
    val to_addresses: List<Address>
)