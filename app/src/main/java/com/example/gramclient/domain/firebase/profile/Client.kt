package com.example.gramclient.domain.firebase.profile

import androidx.room.ColumnInfo

data class Client(
    @ColumnInfo
    val active_orders: List<Int>?=null
)