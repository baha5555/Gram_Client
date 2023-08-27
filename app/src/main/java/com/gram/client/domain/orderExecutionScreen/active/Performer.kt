package com.gram.client.domain.orderExecutionScreen.active

data class Performer(
    val first_name: String,
    val last_name: String,
    val phone: String,
    val transport: Transport?
)