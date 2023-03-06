package com.gram.client.domain.orderHistory

data class Data(
    val allowances: List<Allowance>?=null,
    val comment: Any?=null,
    val created_at: String?=null,
    val dop_phone: Any?=null,
    val filing_time: String?=null,
    val from_address: FromAddress?=null,
    val id: Int?=null,
    val meeting_info: Any?=null,
    val performer: Performer?=null,
    val phone: String?=null,
    val price: Int?=null,
    val search_address_id: Int?=null,
    val status: String?=null,
    val tariff: String?=null,
    val tariff_id: Int?=null,
    val to_addresses: List<ToAddresse>?=null
)