package com.example.gramclient.domain.orderHistory

data class Result(
    val `data`: List<Data>,
    val pagination: Pagination?=null
)