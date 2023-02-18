package com.example.gramclient.domain.orderHistory

data class Pagination(
    val count: Int?=null,
    val count_orders: Int?=null,
    val currentPage: String?=null,
    val current_page: Int?=null,
    val first_page: String?=null,
    val last_page: String?=null,
    val next_page: String?=null,
    val per_page: Int?=null,
    val prev_page: String?=null,
    val total_pages: Int?=null
)