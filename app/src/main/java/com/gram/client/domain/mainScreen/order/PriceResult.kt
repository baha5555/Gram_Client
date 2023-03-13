package com.gram.client.domain.mainScreen.order

data class PriceResult(
    val allowance_percents_price: Int,
    val amount: Int,
    val distance: Double,
    val tariff_id: Int,
    val type_order: Int
)