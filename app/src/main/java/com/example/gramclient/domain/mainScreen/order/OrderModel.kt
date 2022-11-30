package com.example.gramclient.domain.mainScreen.order

data class OrderModel(
    val dop_phone: String? = null,
    val from_address: Int? = null,
    val to_addresses: List<AddressModel>? = null,
    val comment: String? = null,
    val tariff_id: Int,
    val allowances: String? = null
)
