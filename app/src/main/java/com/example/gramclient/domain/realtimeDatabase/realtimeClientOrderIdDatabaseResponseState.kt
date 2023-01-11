package com.example.gramclient.domain.realtimeDatabase

import androidx.lifecycle.LiveData
import com.example.gramclient.domain.realtimeDatabase.Order.RealtimeDatabaseOrder
import com.example.gramclient.domain.realtimeDatabase.profile.Client

data class realtimeClientOrderIdDatabaseResponseState(
    val isLoading: Boolean = false,
    var response:LiveData<Client>? = null,
    val error: String = ""
)
