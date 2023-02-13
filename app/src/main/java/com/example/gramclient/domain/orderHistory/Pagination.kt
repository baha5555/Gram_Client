package com.example.gramclient.domain.orderHistory

data class Pagination(
    val count: Int,
    val currentPage: String,
    val current_page: Int,
    val first_page: String,
    val last_page: String,
    val next_page: String,
    val per_page: Int,
    val prev_page: String,
    val total: Int,
    val total_pages: Int
)