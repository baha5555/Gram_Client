package com.gram.client.domain.mainScreen.fast_address

data class Result(
    val id: Int,
    val address: String,
    var address_lat: String="",
    var address_lng: String="",
    var city: String=""
)