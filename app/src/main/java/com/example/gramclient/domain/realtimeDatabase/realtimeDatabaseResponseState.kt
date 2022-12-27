package com.example.gramclient.domain.realtimeDatabase

import androidx.lifecycle.LiveData
import com.example.gramclient.domain.realtimeDatabase.Order.RealtimeDatabaseOrder

data class realtimeDatabaseResponseState(
    val isLoading: Boolean = false,
    var response: LiveData<List<RealtimeDatabaseOrder>>? = null,
    val error: String = ""
)
