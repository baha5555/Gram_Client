package com.example.gramclient.domain.mainScreen

data class Address(
    var address: String="",
    var id: Int=0,
    var address_lat: String="",
    var address_lng: String="",
    var city: String="",
    var idIncrement: Int = 0
//    val district: String,
//    val model: String,
//    val region: String,
//    val street: String,
//    val synonym_street: Any,
//    val synonyms: Any,
//    val village: String
)
data class Address2(
    var address: String="",
    var id: Int=0
)