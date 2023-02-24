package com.example.gramclient.domain.mainScreen

data class Address(
    var address: String="",
    var id: Int=-1,
    var address_lat: String="",
    var address_lng: String="",
    var city: String="",
    var idIncrement: Int = 0,
)