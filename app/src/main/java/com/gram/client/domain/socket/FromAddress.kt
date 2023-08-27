package com.gram.client.domain.socket

data class FromAddress(
    val address: String,
    val address_lat: String,
    val address_lng: String,
    val city: String,
    val id: Int
)