package com.example.gramclient.domain.mainScreen

data class Address(
    val city: String,
    val district: String,
    val id: Int,
    val lat: String,
    val lng: String,
    val model: String,
    val name: String,
    val region: String,
    val street: String,
    val synonym_street: Any,
    val synonyms: Any,
    val village: String
)