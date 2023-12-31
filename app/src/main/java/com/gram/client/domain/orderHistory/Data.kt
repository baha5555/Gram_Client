package com.gram.client.domain.orderHistory

import com.gram.client.domain.mainScreen.Address

data class Data(
    val allowances: List<Allowance>?=null,
    val comment: Any?=null,
    val created_at: String?=null,
    val dop_phone: Any?=null,
    val filing_time: String?=null,
    val from_address: Address?=null,
    val id: Int?=null,
    val meeting_info: Any?=null,
    val performer: Performer?=null,
    val phone: String?=null,
    val price: Double?=null,
    val search_address_id: Int?=null,
    val status: String?=null,
    val tariff: String?=null,
    val tariff_id: Int?=null,
    val to_addresses: List<Address>?=null
)