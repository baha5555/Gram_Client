package com.example.gramclient.domain.realtimeDatabase

import androidx.lifecycle.LiveData
import com.example.firebaserealtimedatabase.orders.Order

data class realtimeDatabaseResponseState(
    val isLoading: Boolean = false,
    var response: LiveData<List<Order>>? = null,
    val error: String = ""
)
