package com.gram.client.domain.countries

data class ResultKey(
    val capital: Any,
    val country_code: Any,
    val created_at: String,
    val currency: String,
    val currency_name: String,
    val currency_symbol: CurrencySymbol,
    val flag: Int,
    val id: Int,
    val iso2: String,
    val name: String,
    val phone_code: String
)