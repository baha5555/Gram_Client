package com.gram.client.domain.orderHistory

data class Result(
    val `data`: List<Data>,
    val pagination: Pagination?=null
)