package com.example.gramclient.domain.realtimeDatabase.Order

data class RealtimeDatabasePerformer(
    val first_name: String? = null,
    val last_name: String? = null,
    val phone: String? = null,
    val transport: RealtimeDatabaseTransport? = null
)