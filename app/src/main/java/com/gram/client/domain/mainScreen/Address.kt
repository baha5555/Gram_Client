package com.gram.client.domain.mainScreen

data class Address(
    var address: String="",
    var id: Int=0,
    var address_lat: String="",
    var address_lng: String="",
    var city: String="",
    var idIncrement: Int = 0,
    val addr_icon: Any? = null,
    val addr_relation: Any? = null,
    val district: String?= null,
    val region: String?= null,
    val street: String?= null,
    val street_type: String?= null,
    val street_type_short: String?= null,
    val type: String = "",
    val village: String? = null
)