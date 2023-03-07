package com.gram.client.domain.firebase.order

data class ToAddresse(
    val id: Int=0,
    val address_lat: String="",
    val address_lng: String="",
    val address: String="",
    val city: String = ""
)