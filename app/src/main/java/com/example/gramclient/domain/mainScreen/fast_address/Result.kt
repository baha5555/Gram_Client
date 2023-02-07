package com.example.gramclient.domain.mainScreen.fast_address

data class Result(
    val address: String,
    val address_lat: String,
    val address_lng: String,
    val city: String,
    val district: String,
    val id: Int,
    val model: String,
    val region: String,
    val street: String,
    val synonym_street: Any,
    val synonyms: Any,
    val village: String
)