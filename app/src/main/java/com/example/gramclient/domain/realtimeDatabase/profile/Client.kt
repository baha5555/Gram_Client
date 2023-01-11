package com.example.gramclient.domain.realtimeDatabase.profile

import androidx.room.ColumnInfo

data class Client(
    @ColumnInfo
    val active_orders: List<Int>?=null
)