package com.example.gramclient.domain.mainScreen

data class Allowance(
    val allowance_id: Int,
    val name: String,
    val price: Int,
    val tariff: String,
    val tariff_id: Int
)